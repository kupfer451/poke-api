package com.pokestore.poke_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "Información de una orden de compra")
public class OrderDTO {

    @Schema(description = "ID único de la orden", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "ID del usuario que realizó la orden")
    @JsonProperty("user_id")
    private UUID userId;

    @Schema(description = "Estado de la orden", example = "pending",
            allowableValues = {"pending", "paid", "processing", "shipped", "delivered", "cancelled"})
    private String status;

    @Schema(description = "Monto total de la orden", example = "59970.00")
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @Schema(description = "Dirección de envío", example = "Av. Providencia 1234, Santiago")
    @JsonProperty("shipping_address")
    private String shippingAddress;

    @Schema(description = "Notas adicionales del pedido", example = "Entregar en horario de tarde")
    private String notes;

    @Schema(description = "Fecha de creación de la orden")
    @JsonProperty("created_at")
    private Instant createdAt;

    @Schema(description = "Fecha de última actualización")
    @JsonProperty("updated_at")
    private Instant updatedAt;

    @Schema(description = "Items de la orden (productos)")
    private List<OrderItemDTO> items;

    public OrderDTO() {
    }

    public OrderDTO(UUID id, UUID userId, String status, BigDecimal totalAmount, 
                    String shippingAddress, String notes, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
}
