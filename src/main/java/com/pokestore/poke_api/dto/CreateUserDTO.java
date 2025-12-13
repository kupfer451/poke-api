package com.pokestore.poke_api.dto;

public class CreateUserDTO {

    private String username;
    private String password;
    private String email;
    private String rut;

    public CreateUserDTO() {
    }

    public CreateUserDTO(String username, String password, String email, String rut) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.rut = rut;
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
