package com.pokestore.poke_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de operaciones de autenticación")
public class AuthResponseDTO {

    @Schema(description = "Indica si la operación fue exitosa", example = "true")
    private boolean success;

    @Schema(description = "Mensaje descriptivo del resultado", example = "Login exitoso")
    private String message;

    @Schema(description = "Datos del usuario")
    private UserDTO user;

    @Schema(description = "Token JWT para autenticación", example = "eyJhbGciOiJIUzI1NiIs...")
    private String token;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(boolean success, String message, UserDTO user, String token) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.token = token;
    }

    public static AuthResponseDTO success(String message, UserDTO user, String token) {
        return new AuthResponseDTO(true, message, user, token);
    }

    public static AuthResponseDTO error(String message) {
        return new AuthResponseDTO(false, message, null, null);
    }

    // Getters y Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
