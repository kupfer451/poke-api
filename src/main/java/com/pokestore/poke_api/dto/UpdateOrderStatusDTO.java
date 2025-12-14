package com.pokestore.poke_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para actualizar el estado de una orden")
public class UpdateOrderStatusDTO {

    @Schema(description = "Nuevo estado de la orden", example = "shipped",
            allowableValues = {"pending", "paid", "processing", "shipped", "delivered", "cancelled"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String status;

    public UpdateOrderStatusDTO() {
    }

    public UpdateOrderStatusDTO(String status) {
        this.status = status;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
