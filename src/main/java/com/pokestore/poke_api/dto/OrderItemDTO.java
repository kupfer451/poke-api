package com.pokestore.poke_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Item de una orden (producto y cantidad)")
public class OrderItemDTO {

    @Schema(description = "ID único del item", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "ID de la orden a la que pertenece")
    @JsonProperty("order_id")
    private UUID orderId;

    @Schema(description = "ID del producto")
    @JsonProperty("product_id")
    private UUID productId;

    @Schema(description = "Cantidad del producto", example = "2")
    private Integer quantity;

    @Schema(description = "Precio unitario al momento de la compra", example = "19990.00")
    @JsonProperty("unit_price")
    private BigDecimal unitPrice;

    @Schema(description = "Subtotal (quantity * unit_price)", example = "39980.00")
    private BigDecimal subtotal;

    @Schema(description = "Fecha de creación del item")
    @JsonProperty("created_at")
    private Instant createdAt;

    // Datos del producto (para respuestas enriquecidas)
    @Schema(description = "Nombre del producto (solo en respuestas)")
    @JsonProperty("product_name")
    private String productName;

    public OrderItemDTO() {
    }

    public OrderItemDTO(UUID id, UUID orderId, UUID productId, Integer quantity, BigDecimal unitPrice, BigDecimal subtotal, Instant createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
        this.createdAt = createdAt;
    }

    // Getters y Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
}
