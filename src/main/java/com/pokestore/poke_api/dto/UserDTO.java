package com.pokestore.poke_api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserDTO {

    private UUID id;
    private String username;
    private String email;
    private String rut;
    private LocalDateTime createdAt;

    // Constructor vacío
    public UserDTO() {
    }

    // Constructor completo (sin password para seguridad)
    public UserDTO(UUID id, String username, String email, String rut, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.rut = rut;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
