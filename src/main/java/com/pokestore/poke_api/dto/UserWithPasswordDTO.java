package com.pokestore.poke_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.UUID;

/**
 * DTO interno para leer usuarios con hash_pass desde Supabase
 */
public class UserWithPasswordDTO {

    private UUID id;
    private String username;
    
    @JsonProperty("hash_pass")
    private String hashPass;
    
    private String email;
    private String rut;
    
    @JsonProperty("isAdmin")
    private Boolean isAdmin;
    
    @JsonProperty("created_at")
    private Instant createdAt;

    public UserWithPasswordDTO() {
    }

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

    public String getHashPass() {
        return hashPass;
    }

    public void setHashPass(String hashPass) {
        this.hashPass = hashPass;
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

    /**
     * Convierte a UserDTO (sin password)
     */
    public UserDTO toUserDTO() {
        return new UserDTO(id, username, email, rut, isAdmin != null ? isAdmin : false, createdAt);
    }
}
