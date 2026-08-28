package ec.edu.uteq.appweb.biblioteca.repository;

import ec.edu.uteq.appweb.biblioteca.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    boolean existsByNombreIgnoreCase(String nombre);
}
