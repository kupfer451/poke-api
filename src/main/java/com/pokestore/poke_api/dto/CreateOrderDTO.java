package com.pokestore.poke_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Datos para crear una nueva orden")
public class CreateOrderDTO {

    @Schema(description = "Dirección de envío", example = "Av. Providencia 1234, Santiago", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("shipping_address")
    private String shippingAddress;

    @Schema(description = "Notas adicionales del pedido", example = "Entregar en horario de tarde")
    private String notes;

    @Schema(description = "Lista de productos y cantidades", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<CreateOrderItemDTO> items;

    public CreateOrderDTO() {
    }

    public CreateOrderDTO(String shippingAddress, String notes, List<CreateOrderItemDTO> items) {
        this.shippingAddress = shippingAddress;
        this.notes = notes;
        this.items = items;
    }

    // Getters y Setters
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public List<CreateOrderItemDTO> getItems() { return items; }
    public void setItems(List<CreateOrderItemDTO> items) { this.items = items; }
}
