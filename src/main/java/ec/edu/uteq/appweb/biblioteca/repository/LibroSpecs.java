package ec.edu.uteq.appweb.biblioteca.repository;

import ec.edu.uteq.appweb.biblioteca.domain.Libro;
import org.springframework.data.jpa.domain.Specification;

/**
 * Predicados reutilizables del catalogo (Unidad III). COMPLETO.
 *
 * Cuando un filtro no viene informado se devuelve Specification.unrestricted(),
 * que Spring Data elimina al componer. Es importante NO devolver null: desde
 * Spring Data JPA 4.0 la API dejo de aceptar nulos y unrestricted() es su
 * reemplazo documentado.
 *
 * Se usa Specification en lugar de un @Query con parametros anulables porque
 * en PostgreSQL una expresion ":parametro is null" sobre un parametro sin tipo
 * rompe la inferencia del driver con "could not determine data type of parameter".
 */
public final class LibroSpecs {

    private LibroSpecs() {
    }

    public static Specification<Libro> soloActivos() {
        return (raiz, consulta, cb) -> cb.isTrue(raiz.get("activo"));
    }

    public static Specification<Libro> tituloContiene(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            return Specification.unrestricted();
        }
        String patron = "%" + titulo.toLowerCase() + "%";
        return (raiz, consulta, cb) -> cb.like(cb.lower(raiz.get("titulo")), patron);
    }

    public static Specification<Libro> deCategoria(Long categoriaId) {
        if (categoriaId == null) {
            return Specification.unrestricted();
        }
        return (raiz, consulta, cb) -> cb.equal(raiz.get("categoria").get("id"), categoriaId);
    }

    public static Specification<Libro> publicadoDesde(Integer anioDesde) {
        if (anioDesde == null) {
            return Specification.unrestricted();
        }
        return (raiz, consulta, cb) -> cb.greaterThanOrEqualTo(raiz.get("anioPublicacion"), anioDesde);
    }
}
