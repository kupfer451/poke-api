package com.pokestore.poke_api.controller;

import com.pokestore.poke_api.dto.CreateOrderDTO;
import com.pokestore.poke_api.dto.OrderDTO;
import com.pokestore.poke_api.dto.UpdateOrderStatusDTO;
import com.pokestore.poke_api.security.AdminOnly;
import com.pokestore.poke_api.security.Authenticated;
import com.pokestore.poke_api.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Órdenes", description = "Gestión de órdenes de compra")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ============ ENDPOINTS PARA USUARIOS AUTENTICADOS ============

    @Operation(
            summary = "Crear nueva orden",
            description = "Crea una nueva orden de compra para el usuario autenticado. **Requiere autenticación.**"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Orden creada exitosamente",
                    content = @Content(schema = @Schema(implementation = OrderDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o carrito vacío"),
            @ApiResponse(responseCode = "401", description = "Token de autenticación requerido")
    })
    @Authenticated
    @PostMapping
    public Mono<ResponseEntity<OrderDTO>> createOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la orden a crear",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateOrderDTO.class))
            )
            @RequestBody CreateOrderDTO createOrderDTO,
            HttpServletRequest request) {
        
        String userId = (String) request.getAttribute("userId");
        
        return orderService.createOrder(userId, createOrderDTO)
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalArgumentException.class, e ->
                        Mono.just(ResponseEntity.badRequest().build()));
    }

    @Operation(
            summary = "Obtener mis órdenes",
            description = "Obtiene todas las órdenes del usuario autenticado. **Requiere autenticación.**"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de órdenes del usuario",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderDTO.class)))
            ),
            @ApiResponse(responseCode = "401", description = "Token de autenticación requerido")
    })
    @Authenticated
    @GetMapping("/my")
    public Mono<ResponseEntity<List<OrderDTO>>> getMyOrders(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        
        return orderService.getOrdersByUserId(userId)
                .map(ResponseEntity::ok);
    }

    @Operation(
            summary = "Obtener orden por ID",
            description = "Obtiene los detalles de una orden específica. " +
                    "Los usuarios solo pueden ver sus propias órdenes. Los admins pueden ver todas. **Requiere autenticación.**"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Orden encontrada",
                    content = @Content(schema = @Schema(implementation = OrderDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "Token de autenticación requerido"),
            @ApiResponse(responseCode = "403", description = "No tienes permiso para ver esta orden"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    @Authenticated
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> getOrderById(
            @Parameter(description = "ID de la orden", required = true)
            @PathVariable String id,
            HttpServletRequest request) {
        
        String userId = (String) request.getAttribute("userId");
        Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");

        return orderService.getOrderById(id)
                .map(order -> {
                    // Verificar que el usuario sea el dueño o sea admin
                    if (!Boolean.TRUE.equals(isAdmin) && !order.getUserId().toString().equals(userId)) {
                        return ResponseEntity.status(403)
                                .body((Object) Map.of("error", "Forbidden", 
                                        "message", "No tienes permiso para ver esta orden"));
                    }
                    return ResponseEntity.ok((Object) order);
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @Operation(
            summary = "Cancelar orden",
            description = "Cancela una orden. Los usuarios solo pueden cancelar sus órdenes en estado 'pending' o 'paid'. " +
                    "Los admins pueden cancelar cualquier orden. **Requiere autenticación.**"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Orden cancelada exitosamente",
                    content = @Content(schema = @Schema(implementation = OrderDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "La orden no puede ser cancelada"),
            @ApiResponse(responseCode = "401", description = "Token de autenticación requerido"),
            @ApiResponse(responseCode = "403", description = "No tienes permiso para cancelar esta orden"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    @Authenticated
    @PostMapping("/{id}/cancel")
    public Mono<ResponseEntity<Object>> cancelOrder(
            @Parameter(description = "ID de la orden a cancelar", required = true)
            @PathVariable String id,
            HttpServletRequest request) {
        
        String userId = (String) request.getAttribute("userId");
        Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");

        return orderService.cancelOrder(id, userId, Boolean.TRUE.equals(isAdmin))
                .map(order -> ResponseEntity.ok((Object) order))
                .onErrorResume(SecurityException.class, e ->
                        Mono.just(ResponseEntity.status(403)
                                .body(Map.of("error", "Forbidden", "message", e.getMessage()))))
                .onErrorResume(IllegalStateException.class, e ->
                        Mono.just(ResponseEntity.badRequest()
                                .body(Map.of("error", "Bad Request", "message", e.getMessage()))))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    // ============ ENDPOINTS SOLO PARA ADMINISTRADORES ============

    @Operation(
            summary = "Obtener todas las órdenes",
            description = "Obtiene todas las órdenes del sistema. **Requiere rol de administrador.**"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de todas las órdenes",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderDTO.class)))
            ),
            @ApiResponse(responseCode = "401", description = "Token de autenticación requerido"),
            @ApiResponse(responseCode = "403", description = "Se requiere rol de administrador")
    })
    @AdminOnly
    @GetMapping
    public Mono<ResponseEntity<List<OrderDTO>>> getAllOrders() {
        return orderService.getAllOrders()
                .map(ResponseEntity::ok);
    }

    @Operation(
            summary = "Obtener órdenes de un usuario",
            description = "Obtiene todas las órdenes de un usuario específico. **Requiere rol de administrador.**"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de órdenes del usuario",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderDTO.class)))
            ),
            @ApiResponse(responseCode = "401", description = "Token de autenticación requerido"),
            @ApiResponse(responseCode = "403", description = "Se requiere rol de administrador")
    })
    @AdminOnly
    @GetMapping("/user/{userId}")
    public Mono<ResponseEntity<List<OrderDTO>>> getOrdersByUser(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable String userId) {
        return orderService.getOrdersByUserId(userId)
                .map(ResponseEntity::ok);
    }

    @Operation(
            summary = "Actualizar estado de orden",
            description = "Actualiza el estado de una orden. Estados válidos: pending, paid, processing, shipped, delivered, cancelled. " +
                    "**Requiere rol de administrador.**"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = OrderDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Estado inválido"),
            @ApiResponse(responseCode = "401", description = "Token de autenticación requerido"),
            @ApiResponse(responseCode = "403", description = "Se requiere rol de administrador"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    @AdminOnly
    @PatchMapping("/{id}/status")
    public Mono<ResponseEntity<Object>> updateOrderStatus(
            @Parameter(description = "ID de la orden", required = true)
            @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevo estado de la orden",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateOrderStatusDTO.class))
            )
            @RequestBody UpdateOrderStatusDTO updateStatusDTO) {
        
        return orderService.updateOrderStatus(id, updateStatusDTO.getStatus())
                .map(order -> ResponseEntity.ok((Object) order))
                .onErrorResume(IllegalArgumentException.class, e ->
                        Mono.just(ResponseEntity.badRequest()
                                .body(Map.of("error", "Bad Request", "message", e.getMessage()))))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @Operation(
            summary = "Eliminar orden",
            description = "Elimina permanentemente una orden y todos sus items. **Requiere rol de administrador.**"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Orden eliminada exitosamente"),
            @ApiResponse(responseCode = "401", description = "Token de autenticación requerido"),
            @ApiResponse(responseCode = "403", description = "Se requiere rol de administrador"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    @AdminOnly
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteOrder(
            @Parameter(description = "ID de la orden a eliminar", required = true)
            @PathVariable String id) {
        return orderService.deleteOrder(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }
}
