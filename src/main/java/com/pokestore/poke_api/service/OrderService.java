package com.pokestore.poke_api.service;

import com.pokestore.poke_api.dto.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderService {

    private final SupabaseService supabaseService;

    // Estados válidos para las órdenes
    private static final Set<String> VALID_STATUSES = Set.of(
            "pending", "paid", "processing", "shipped", "delivered", "cancelled"
    );

    public OrderService(SupabaseService supabaseService) {
        this.supabaseService = supabaseService;
    }

    /**
     * Obtener todas las órdenes (solo admin)
     */
    public Mono<List<OrderDTO>> getAllOrders() {
        return supabaseService.selectAll("orders", new ParameterizedTypeReference<List<OrderDTO>>() {})
                .flatMap(this::enrichOrdersWithItems);
    }

    /**
     * Obtener órdenes de un usuario específico
     */
    public Mono<List<OrderDTO>> getOrdersByUserId(String userId) {
        String filter = "user_id=eq." + userId;
        return supabaseService.selectWithFilter("orders", filter, new ParameterizedTypeReference<List<OrderDTO>>() {})
                .flatMap(this::enrichOrdersWithItems);
    }

    /**
     * Obtener una orden por ID
     */
    public Mono<OrderDTO> getOrderById(String orderId) {
        return supabaseService.selectById("orders", orderId, new ParameterizedTypeReference<List<OrderDTO>>() {})
                .flatMap(orders -> {
                    if (orders == null || orders.isEmpty()) {
                        return Mono.empty();
                    }
                    return enrichOrderWithItems(orders.get(0));
                });
    }

    /**
     * Crear una nueva orden
     */
    public Mono<OrderDTO> createOrder(String userId, CreateOrderDTO createOrderDTO) {
        if (createOrderDTO.getItems() == null || createOrderDTO.getItems().isEmpty()) {
            return Mono.error(new IllegalArgumentException("La orden debe tener al menos un producto"));
        }

        // Primero obtener los precios de los productos
        return getProductPrices(createOrderDTO.getItems())
                .flatMap(productPrices -> {
                    // Calcular el total
                    BigDecimal totalAmount = calculateTotal(createOrderDTO.getItems(), productPrices);

                    // Crear la orden en la base de datos
                    Map<String, Object> orderData = new HashMap<>();
                    orderData.put("user_id", userId);
                    orderData.put("status", "pending");
                    orderData.put("total_amount", totalAmount);
                    orderData.put("shipping_address", createOrderDTO.getShippingAddress());
                    orderData.put("notes", createOrderDTO.getNotes());

                    return supabaseService.insert("orders", orderData, new ParameterizedTypeReference<List<OrderDTO>>() {})
                            .flatMap(orders -> {
                                if (orders == null || orders.isEmpty()) {
                                    return Mono.error(new RuntimeException("Error al crear la orden"));
                                }
                                OrderDTO createdOrder = orders.get(0);

                                // Crear los items de la orden
                                return createOrderItems(createdOrder.getId().toString(), createOrderDTO.getItems(), productPrices)
                                        .then(enrichOrderWithItems(createdOrder));
                            });
                });
    }

    /**
     * Actualizar el estado de una orden (solo admin)
     */
    public Mono<OrderDTO> updateOrderStatus(String orderId, String newStatus) {
        if (!VALID_STATUSES.contains(newStatus)) {
            return Mono.error(new IllegalArgumentException("Estado inválido: " + newStatus + 
                    ". Estados válidos: " + VALID_STATUSES));
        }

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("status", newStatus);

        return supabaseService.update("orders", orderId, updateData, new ParameterizedTypeReference<List<OrderDTO>>() {})
                .flatMap(orders -> {
                    if (orders == null || orders.isEmpty()) {
                        return Mono.empty();
                    }
                    return enrichOrderWithItems(orders.get(0));
                });
    }

    /**
     * Cancelar una orden (usuario puede cancelar sus propias órdenes pendientes)
     */
    public Mono<OrderDTO> cancelOrder(String orderId, String userId, boolean isAdmin) {
        return getOrderById(orderId)
                .flatMap(order -> {
                    // Verificar que el usuario sea el dueño o sea admin
                    if (!isAdmin && !order.getUserId().toString().equals(userId)) {
                        return Mono.error(new SecurityException("No tienes permiso para cancelar esta orden"));
                    }

                    // Solo se pueden cancelar órdenes pendientes o pagadas
                    if (!isAdmin && !order.getStatus().equals("pending") && !order.getStatus().equals("paid")) {
                        return Mono.error(new IllegalStateException(
                                "Solo se pueden cancelar órdenes en estado 'pending' o 'paid'"));
                    }

                    return updateOrderStatus(orderId, "cancelled");
                });
    }

    /**
     * Eliminar una orden (solo admin)
     */
    public Mono<Void> deleteOrder(String orderId) {
        // Primero eliminar los items de la orden
        return supabaseService.deleteWithFilter("order_items", "order_id=eq." + orderId)
                .then(supabaseService.delete("orders", orderId));
    }

    // ============ Métodos auxiliares ============

    /**
     * Obtener los precios de los productos
     */
    private Mono<Map<String, BigDecimal>> getProductPrices(List<CreateOrderItemDTO> items) {
        List<String> productIds = items.stream()
                .map(item -> item.getProductId().toString())
                .toList();

        String filter = "id=in.(" + String.join(",", productIds) + ")";
        
        return supabaseService.selectWithFilter("products", filter, new ParameterizedTypeReference<List<ProductDTO>>() {})
                .map(products -> {
                    Map<String, BigDecimal> prices = new HashMap<>();
                    for (ProductDTO product : products) {
                        prices.put(product.getId().toString(), product.getPrice());
                    }
                    return prices;
                });
    }

    /**
     * Calcular el total de la orden
     */
    private BigDecimal calculateTotal(List<CreateOrderItemDTO> items, Map<String, BigDecimal> productPrices) {
        BigDecimal total = BigDecimal.ZERO;
        for (CreateOrderItemDTO item : items) {
            BigDecimal price = productPrices.get(item.getProductId().toString());
            if (price != null) {
                total = total.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }
        return total;
    }

    /**
     * Crear los items de una orden
     */
    private Mono<Void> createOrderItems(String orderId, List<CreateOrderItemDTO> items, Map<String, BigDecimal> productPrices) {
        List<Mono<Void>> insertions = new ArrayList<>();

        for (CreateOrderItemDTO item : items) {
            BigDecimal unitPrice = productPrices.get(item.getProductId().toString());
            if (unitPrice == null) {
                continue; // Producto no encontrado, saltarlo
            }

            Map<String, Object> itemData = new HashMap<>();
            itemData.put("order_id", orderId);
            itemData.put("product_id", item.getProductId().toString());
            itemData.put("quantity", item.getQuantity());
            itemData.put("unit_price", unitPrice);

            Mono<Void> insertion = supabaseService.insert("order_items", itemData, 
                    new ParameterizedTypeReference<List<OrderItemDTO>>() {})
                    .then();
            insertions.add(insertion);
        }

        return Mono.when(insertions);
    }

    /**
     * Enriquecer una orden con sus items
     */
    private Mono<OrderDTO> enrichOrderWithItems(OrderDTO order) {
        String filter = "order_id=eq." + order.getId().toString();
        return supabaseService.selectWithFilter("order_items", filter, 
                new ParameterizedTypeReference<List<OrderItemDTO>>() {})
                .map(items -> {
                    order.setItems(items);
                    return order;
                });
    }

    /**
     * Enriquecer múltiples órdenes con sus items
     */
    private Mono<List<OrderDTO>> enrichOrdersWithItems(List<OrderDTO> orders) {
        if (orders == null || orders.isEmpty()) {
            return Mono.just(Collections.emptyList());
        }

        List<Mono<OrderDTO>> enrichedOrders = orders.stream()
                .map(this::enrichOrderWithItems)
                .toList();

        return Mono.zip(enrichedOrders, results -> {
            List<OrderDTO> list = new ArrayList<>();
            for (Object result : results) {
                list.add((OrderDTO) result);
            }
            return list;
        });
    }
}
