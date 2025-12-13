package com.pokestore.poke_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Información pública del usuario (sin contraseña)")
public class UserDTO {

    @Schema(
            description = "ID único del usuario",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID id;

    @Schema(
            description = "Nombre de usuario",
            example = "juanperez"
    )
    private String username;

    @Schema(
            description = "Email del usuario",
            example = "juan@ejemplo.com"
    )
    private String email;

    @Schema(
            description = "RUT chileno del usuario",
            example = "12345678-9"
    )
    private String rut;

    @Schema(
            description = "Indica si el usuario es administrador",
            example = "false"
    )
    @JsonProperty("isAdmin")
    private Boolean isAdmin;

    @Schema(
            description = "Fecha y hora de creación del usuario",
            example = "2025-01-15T10:30:00Z"
    )
    @JsonProperty("created_at")
    private Instant createdAt;

    // Constructor vacío
    public UserDTO() {
    }

    // Constructor completo (sin password para seguridad)
    public UserDTO(UUID id, String username, String email, String rut, Boolean isAdmin, Instant createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.rut = rut;
        this.isAdmin = isAdmin != null ? isAdmin : false;
        this.createdAt = createdAt;
    }

    // Getters y Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
