package pe.sanhiplast.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.sanhiplast.auth.domain.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
}
