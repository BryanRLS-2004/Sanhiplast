package pe.sanhiplast.auth.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pe.sanhiplast.auth.domain.Rol;
import pe.sanhiplast.auth.repository.RolRepository;

/** Siembra los roles base del sistema (RF02: administrador y vendedor). */
@Component
public class DataSeeder implements CommandLineRunner {

    private final RolRepository rolRepository;

    public DataSeeder(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public void run(String... args) {
        crearRolSiNoExiste("ADMINISTRADOR");
        crearRolSiNoExiste("VENDEDOR");
    }

    private void crearRolSiNoExiste(String nombre) {
        rolRepository.findByNombreIgnoreCase(nombre).orElseGet(() -> {
            Rol rol = new Rol();
            rol.setNombre(nombre);
            return rolRepository.save(rol);
        });
    }
}
