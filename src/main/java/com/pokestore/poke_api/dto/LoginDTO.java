package com.pokestore.poke_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos requeridos para iniciar sesión")
public class LoginDTO {

    @Schema(
            description = "Email del usuario registrado",
            example = "usuario@ejemplo.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @Schema(
            description = "Contraseña del usuario",
            example = "miContraseña123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;

    public LoginDTO() {
    }

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
