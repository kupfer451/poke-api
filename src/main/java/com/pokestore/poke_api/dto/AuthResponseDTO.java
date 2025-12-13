package com.pokestore.poke_api.dto;

public class AuthResponseDTO {

    private boolean success;
    private String message;
    private UserDTO user;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(boolean success, String message, UserDTO user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public static AuthResponseDTO success(String message, UserDTO user) {
        return new AuthResponseDTO(true, message, user);
    }

    public static AuthResponseDTO error(String message) {
        return new AuthResponseDTO(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
