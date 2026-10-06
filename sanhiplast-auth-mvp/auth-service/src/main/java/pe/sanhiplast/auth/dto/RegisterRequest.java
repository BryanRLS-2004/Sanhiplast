package pe.sanhiplast.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank String nombre,
        @NotBlank String apellido,
        String telefono,
        String direccion,
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String rol // ADMINISTRADOR o VENDEDOR
) {
}
