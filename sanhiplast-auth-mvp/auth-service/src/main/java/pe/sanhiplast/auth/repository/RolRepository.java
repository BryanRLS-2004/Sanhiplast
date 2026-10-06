package pe.sanhiplast.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.sanhiplast.auth.domain.Rol;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombreIgnoreCase(String nombre);
}
