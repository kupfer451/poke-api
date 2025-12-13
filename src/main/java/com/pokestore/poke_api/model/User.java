package com.pokestore.poke_api.model;

import java.time.Instant;
import java.util.UUID;

/**
 * User model for Supabase REST API integration
 */
public class User {

    private UUID id;
    private String username;
    private String hashPass;
    private String email;
    private String rut;
    private Boolean isAdmin;
    private Instant createdAt;

    // Constructores
    public User() {
    }

    public User(String username, String hashPass, String email, String rut, Boolean isAdmin) {
        this.username = username;
        this.hashPass = hashPass;
        this.email = email;
        this.rut = rut;
        this.isAdmin = isAdmin != null ? isAdmin : false;
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
}
