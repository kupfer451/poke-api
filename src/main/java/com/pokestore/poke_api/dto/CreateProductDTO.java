package com.pokestore.poke_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Datos para crear un nuevo producto")
public class CreateProductDTO {

    @Schema(description = "Nombre del producto", example = "Peluche Pikachu 30cm", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("product_name")
    private String productName;

    @Schema(description = "Precio del producto en pesos chilenos", example = "29990.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @Schema(description = "Cantidad disponible en stock", example = "50", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantity;

    @Schema(description = "Descripción detallada del producto", example = "Peluche oficial de Pikachu")
    private String description;

    @Schema(description = "Categoría del producto", example = "Peluches")
    private String category;

    @Schema(description = "URL de la imagen del producto", example = "https://ejemplo.com/imagen.jpg")
    @JsonProperty("image_url")
    private String imageUrl;

    public CreateProductDTO() {
    }

    public CreateProductDTO(String productName, BigDecimal price, Integer quantity, String description, String category, String imageUrl) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    // Getters y Setters
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
