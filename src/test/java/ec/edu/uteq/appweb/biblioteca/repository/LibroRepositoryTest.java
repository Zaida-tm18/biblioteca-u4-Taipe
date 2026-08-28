package ec.edu.uteq.appweb.biblioteca.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ec.edu.uteq.appweb.biblioteca.BaseIntegracionTest;
import ec.edu.uteq.appweb.biblioteca.domain.Libro;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * Prueba de ejemplo YA IMPLEMENTADA sobre la capa de datos de la Unidad III.
 * Sirve de plantilla para las pruebas que usted debe escribir en la Unidad IV.
 */
class LibroRepositoryTest extends BaseIntegracionTest {

    @Autowired
    private LibroRepository libros;

    @Test
    @DisplayName("La semilla carga al menos 50 libros activos")
    void laSemillaCargaElCatalogo() {
        Page<Libro> pagina = libros.findByActivoTrue(PageRequest.of(0, 10));
        assertThat(pagina.getTotalElements()).isGreaterThanOrEqualTo(50);
        assertThat(pagina.getContent()).hasSize(10);
    }

    @Test
    @DisplayName("La busqueda por titulo parcial ignora mayusculas y minusculas")
    void buscaPorTituloSinDistinguirMayusculas() {
        Page<Libro> pagina = libros.findAll(
                org.springframework.data.jpa.domain.Specification.allOf(
                        LibroSpecs.soloActivos(), LibroSpecs.tituloContiene("clean")),
                PageRequest.of(0, 10));
        assertThat(pagina.getContent())
                .isNotEmpty()
                .allSatisfy(libro -> assertThat(libro.getTitulo().toLowerCase()).contains("clean"));
    }

    @Test
    @DisplayName("Un ISBN inexistente devuelve un Optional vacio, no una excepcion")
    void isbnInexistenteDevuelveVacio() {
        assertThat(libros.findByIsbn("0000000000000")).isEmpty();
    }
}
