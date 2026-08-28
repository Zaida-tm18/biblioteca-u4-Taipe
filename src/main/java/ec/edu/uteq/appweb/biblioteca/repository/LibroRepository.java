package ec.edu.uteq.appweb.biblioteca.repository;

import ec.edu.uteq.appweb.biblioteca.domain.Libro;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Capa de acceso a datos del catalogo (Unidad III). COMPLETA.
 *
 * La busqueda con filtros opcionales se resuelve con Specification
 * (JpaSpecificationExecutor) y no con un @Query de parametros anulables.
 * El motivo es concreto: en PostgreSQL una expresion del tipo
 * ":parametro is null" sobre un parametro sin tipo hace fallar la inferencia
 * de tipos del driver con el error "could not determine data type of parameter".
 * Specification construye el predicado en Java y solo agrega al WHERE los
 * filtros que realmente vienen informados.
 */
public interface LibroRepository extends JpaRepository<Libro, Long>, JpaSpecificationExecutor<Libro> {

    Optional<Libro> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    Page<Libro> findByActivoTrue(Pageable pageable);
}
