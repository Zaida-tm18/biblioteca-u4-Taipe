package ec.edu.uteq.appweb.biblioteca.web;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ec.edu.uteq.appweb.biblioteca.BaseIntegracionTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * ============================================================================
 * TODO-U4-5: PRUEBAS DE INTEGRACION  [IMPLEMENTADO - B4]
 * ============================================================================
 *
 * Tres casos de integracion HTTP reales sobre LibroController, heredando de
 * BaseIntegracionTest (PostgreSQL 18 efimero con Testcontainers + Flyway V1-V3).
 *
 * Se anota cada prueba con @WithMockUser para que sigan pasando cuando se cierre
 * el TODO-U4-2 y la cadena de seguridad deje de ser permisiva: el rol declarado
 * es el que exige cada endpoint.
 *
 * Ejecute con:  mvn -B test
 */
class LibroControllerIT extends BaseIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "LECTOR")
    @DisplayName("GET /api/v1/libros responde 200 con las cinco claves del envoltorio y meta.page y meta.size correctos")
    void listarLibrosDevuelveEnvoltorioConMetadatosDePaginacion() throws Exception {
        mockMvc.perform(get("/api/v1/libros")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                // Las cinco claves del envoltorio {success, data, message, errors, meta}
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.meta").exists())
                // La paginacion solicitada se refleja en los metadatos
                .andExpect(jsonPath("$.meta.page").value(0))
                .andExpect(jsonPath("$.meta.size").value(5))
                .andExpect(jsonPath("$.meta.totalElements").isNumber())
                // El size pedido acota realmente el contenido devuelto
                .andExpect(jsonPath("$.data.length()").value(Matchers.lessThanOrEqualTo(5)));
    }

    @Test
    @WithMockUser(roles = "LECTOR")
    @DisplayName("GET /api/v1/libros/999999 responde 404 en formato Problem Details con title, status y detail")
    void libroInexistenteDevuelveProblemDetail() throws Exception {
        mockMvc.perform(get("/api/v1/libros/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Recurso no encontrado"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                // El error NO viaja en el envoltorio de exito: los formatos no se mezclan
                .andExpect(jsonPath("$.success").doesNotExist());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/libros con titulo vacio responde 400 y el arreglo errors no esta vacio")
    void crearLibroConTituloVacioDevuelveErroresDeValidacion() throws Exception {
        String cuerpoInvalido = """
                {
                  "isbn": "9781234567897",
                  "titulo": "",
                  "anioPublicacion": 2020,
                  "ejemplaresTotales": 3,
                  "autorId": 1,
                  "editorialId": 1,
                  "categoriaId": 1
                }
                """;

        mockMvc.perform(post("/api/v1/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Solicitud invalida"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(greaterThan(0)))
                // El campo que fallo es el titulo
                .andExpect(jsonPath("$.errors").value(Matchers.hasItem(Matchers.containsString("titulo"))));
    }
}