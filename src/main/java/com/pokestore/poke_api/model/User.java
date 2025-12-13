package com.pokestore.poke_api.model;

import java.time.LocalDateTime;
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
    private LocalDateTime createdAt;

    // Constructores
    public User() {
    }

    public User(String username, String hashPass, String email, String rut) {
        this.username = username;
        this.hashPass = hashPass;
        this.email = email;
        this.rut = rut;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
