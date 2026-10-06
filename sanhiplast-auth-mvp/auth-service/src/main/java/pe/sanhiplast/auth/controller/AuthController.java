package pe.sanhiplast.auth.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.sanhiplast.auth.dto.LoginRequest;
import pe.sanhiplast.auth.dto.LoginResponse;
import pe.sanhiplast.auth.dto.RegisterRequest;
import pe.sanhiplast.auth.dto.UsuarioResponse;
import pe.sanhiplast.auth.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /** RF01: Iniciar sesion. Devuelve un JWT valido por 60 minutos. */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /** Alta de usuarios con rol (ADMINISTRADOR o VENDEDOR). Usado para poblar datos de prueba. */
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(201).body(authService.register(request));
    }

    /** RF02: endpoint protegido que confirma que el token/rol se valido correctamente. */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(Authentication authentication) {
        return ResponseEntity.ok(Map.of(
                "username", authentication.getName(),
                "authorities", authentication.getAuthorities()
        ));
    }

    /** Endpoint de solo administrador, evidencia de autorizacion por rol (RF02). */
    @GetMapping("/admin/ping")
    public ResponseEntity<Map<String, String>> adminPing() {
        return ResponseEntity.ok(Map.of("mensaje", "Acceso autorizado solo para ADMINISTRADOR"));
    }
}
