package ec.edu.uteq.appweb.biblioteca.repository;

import ec.edu.uteq.appweb.biblioteca.domain.EstadoPrestamo;
import ec.edu.uteq.appweb.biblioteca.domain.Prestamo;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    long countBySocioIdAndEstado(Long socioId, EstadoPrestamo estado);

    List<Prestamo> findBySocioIdAndEstado(Long socioId, EstadoPrestamo estado);

    Page<Prestamo> findByEstado(EstadoPrestamo estado, Pageable pageable);
}
