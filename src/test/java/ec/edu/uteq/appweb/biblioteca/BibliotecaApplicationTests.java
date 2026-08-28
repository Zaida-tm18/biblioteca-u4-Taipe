package ec.edu.uteq.appweb.biblioteca;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Prueba de humo: verifica que el contexto de Spring arranca con la base real,
 * que Flyway aplica las tres migraciones y que no hay beans mal declarados.
 * Si esta prueba falla, no siga: arregle el arranque primero.
 */
class BibliotecaApplicationTests extends BaseIntegracionTest {

    @Test
    @DisplayName("El contexto de la aplicacion arranca correctamente")
    void contextoArranca() {
        // Sin aserciones: si el contexto no carga, la prueba falla sola.
    }
}
