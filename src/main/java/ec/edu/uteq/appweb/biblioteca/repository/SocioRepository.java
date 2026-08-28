package ec.edu.uteq.appweb.biblioteca.repository;

import ec.edu.uteq.appweb.biblioteca.domain.Socio;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocioRepository extends JpaRepository<Socio, Long> {

    Optional<Socio> findByCedula(String cedula);

    boolean existsByCedula(String cedula);

    boolean existsByCorreoIgnoreCase(String correo);

    Page<Socio> findByActivoTrue(Pageable pageable);
}
