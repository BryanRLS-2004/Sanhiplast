package pe.sanhiplast.auth.dto;

public record UsuarioResponse(
        Long idUsuario,
        String username,
        String nombreCompleto,
        String rol,
        Boolean activo
) {
}
