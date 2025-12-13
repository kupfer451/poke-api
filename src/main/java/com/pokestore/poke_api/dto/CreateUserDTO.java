package com.pokestore.poke_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos requeridos para crear un nuevo usuario")
public class CreateUserDTO {

    @Schema(
            description = "Nombre de usuario único",
            example = "juanperez",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String username;

    @Schema(
            description = "Contraseña del usuario (será hasheada antes de almacenar)",
            example = "miContraseña123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;

    @Schema(
            description = "Email único del usuario",
            example = "juan@ejemplo.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @Schema(
            description = "RUT chileno único (con guión y dígito verificador)",
            example = "12345678-9",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String rut;

    @Schema(
            description = "Indica si el usuario tendrá permisos de administrador",
            example = "false",
            defaultValue = "false"
    )
    private Boolean isAdmin;

    public CreateUserDTO() {
    }

    public CreateUserDTO(String username, String password, String email, String rut, Boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.rut = rut;
        this.isAdmin = isAdmin;
    }

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

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
