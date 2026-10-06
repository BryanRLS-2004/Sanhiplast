package pe.sanhiplast.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.sanhiplast.auth.domain.Persona;
import pe.sanhiplast.auth.domain.Rol;
import pe.sanhiplast.auth.domain.Usuario;
import pe.sanhiplast.auth.dto.LoginRequest;
import pe.sanhiplast.auth.dto.LoginResponse;
import pe.sanhiplast.auth.dto.RegisterRequest;
import pe.sanhiplast.auth.dto.UsuarioResponse;
import pe.sanhiplast.auth.repository.PersonaRepository;
import pe.sanhiplast.auth.repository.RolRepository;
import pe.sanhiplast.auth.repository.UsuarioRepository;
import pe.sanhiplast.auth.security.JwtService;

import java.util.Map;

/**
 * Logica de negocio de RF01 (Iniciar sesion), RF02 (Gestionar roles)
 * y RF11 (Cerrar sesion / invalidacion logica del token en el cliente).
 */
@Service
public class AuthService {

    private static final long EXPIRATION_MINUTES = 60;

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PersonaRepository personaRepository,
                        RolRepository rolRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsernameIgnoreCaseAndActivoTrue(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Usuario o contrasena incorrectos"));

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new IllegalArgumentException("Usuario o contrasena incorrectos");
        }

        String token = jwtService.generateToken(usuario.getUsername(), Map.of(
                "rol", usuario.getRol().getNombre(),
                "idUsuario", usuario.getIdUsuario()
        ));

        String nombreCompleto = usuario.getPersona().getNombre() + " " + usuario.getPersona().getApellido();

        return new LoginResponse(token, "Bearer", usuario.getUsername(), nombreCompleto,
                usuario.getRol().getNombre(), EXPIRATION_MINUTES);
    }

    @Transactional
    public UsuarioResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByUsernameIgnoreCase(request.username())) {
            throw new IllegalArgumentException("El usuario/correo ya esta registrado");
        }

        Rol rol = rolRepository.findByNombreIgnoreCase(request.rol())
                .orElseThrow(() -> new IllegalArgumentException("Rol no valido: " + request.rol()));

        Persona persona = new Persona();
        persona.setNombre(request.nombre());
        persona.setApellido(request.apellido());
        persona.setTelefono(request.telefono());
        persona.setDireccion(request.direccion());
        persona = personaRepository.save(persona);

        Usuario usuario = new Usuario();
        usuario.setPersona(persona);
        usuario.setRol(rol);
        usuario.setUsername(request.username());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario = usuarioRepository.save(usuario);

        return new UsuarioResponse(usuario.getIdUsuario(), usuario.getUsername(),
                persona.getNombre() + " " + persona.getApellido(), rol.getNombre(), usuario.getActivo());
    }
}
