package ec.edu.uteq.appweb.biblioteca.repository;

import ec.edu.uteq.appweb.biblioteca.domain.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditorialRepository extends JpaRepository<Editorial, Long> {

    boolean existsByNombreIgnoreCase(String nombre);
}
