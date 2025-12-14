package com.pokestore.poke_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Información del producto")
public class ProductDTO {

    @Schema(
            description = "ID único del producto",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID id;

    @Schema(
            description = "Nombre del producto",
            example = "Peluche Pikachu 30cm"
    )
    @JsonProperty("product_name")
    private String productName;

    @Schema(
            description = "Precio del producto en pesos chilenos",
            example = "29990.00"
    )
    private BigDecimal price;

    @Schema(
            description = "Cantidad disponible en stock",
            example = "50"
    )
    private Integer quantity;

    @Schema(
            description = "Descripción detallada del producto",
            example = "Peluche oficial de Pikachu de 30cm de altura, material suave y de alta calidad"
    )
    private String description;

    @Schema(
            description = "Categoría del producto",
            example = "Peluches"
    )
    private String category;

    @Schema(
            description = "URL de la imagen del producto",
            example = "https://ejemplo.com/imagen.jpg"
    )
    @JsonProperty("image_url")
    private String imageUrl;

    @Schema(
            description = "Fecha y hora de creación del producto",
            example = "2025-01-15T10:30:00Z"
    )
    @JsonProperty("created_at")
    private Instant createdAt;

    public ProductDTO() {
    }

    public ProductDTO(UUID id, String productName, BigDecimal price, Integer quantity, String description, String category, String imageUrl, Instant createdAt) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
