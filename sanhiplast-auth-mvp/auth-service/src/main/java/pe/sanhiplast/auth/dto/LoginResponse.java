package pe.sanhiplast.auth.dto;

public record LoginResponse(
        String token,
        String tipo,
        String username,
        String nombreCompleto,
        String rol,
        long expiraEnMinutos
) {
}
