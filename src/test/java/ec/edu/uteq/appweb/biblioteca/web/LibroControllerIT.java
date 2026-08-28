package ec.edu.uteq.appweb.biblioteca.web;

import ec.edu.uteq.appweb.biblioteca.BaseIntegracionTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ============================================================================
 * TODO-U4-5: PRUEBAS DE INTEGRACION (minimo 10 en total en el proyecto)
 * ============================================================================
 *
 * Quite la anotacion @Disabled a medida que implemente cada caso. La Guia PE-U4
 * exige un minimo de diez pruebas de feature o integracion pasando sin fallos,
 * y pide la captura del resultado de la ejecucion dentro del informe.
 *
 * Casos minimos sugeridos, alineados con la rubrica:
 *   1. Login con credenciales correctas devuelve 200 y un token.
 *   2. Login con credenciales invalidas devuelve 401.
 *   3. GET /api/v1/libros sin token devuelve 401.
 *   4. GET /api/v1/libros con rol LECTOR devuelve 200 y trae meta.
 *   5. POST /api/v1/libros con rol LECTOR devuelve 403.
 *   6. POST /api/v1/libros con rol ADMIN y cuerpo valido devuelve 201 con Location.
 *   7. GET /api/v1/libros/{id} inexistente devuelve 404 con ProblemDetail.
 *   8. POST /api/v1/libros con cuerpo invalido devuelve 400 y el arreglo errors poblado.
 *   9. POST /api/v1/prestamos a un socio con tres prestamos activos devuelve 409.
 *  10. Toda respuesta exitosa trae el envoltorio {success, data, message, errors, meta}.
 *
 * Ejecute con:  mvn test
 */
class LibroControllerIT extends BaseIntegracionTest {

    @Test
    @Disabled("TODO-U4-5: implementar")
    @DisplayName("GET /api/v1/libros responde 200 con envoltorio y metadatos de paginacion")
    void listarLibrosDevuelveEnvoltorio() {
        throw new UnsupportedOperationException("TODO-U4-5");
    }

    @Test
    @Disabled("TODO-U4-5: implementar")
    @DisplayName("POST /api/v1/libros con rol LECTOR responde 403")
    void crearLibroConRolLectorDevuelveProhibido() {
        throw new UnsupportedOperationException("TODO-U4-5");
    }
}
