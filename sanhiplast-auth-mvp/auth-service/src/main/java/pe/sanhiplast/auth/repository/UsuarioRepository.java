package pe.sanhiplast.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.sanhiplast.auth.domain.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsernameIgnoreCaseAndActivoTrue(String username);
    boolean existsByUsernameIgnoreCase(String username);
}
