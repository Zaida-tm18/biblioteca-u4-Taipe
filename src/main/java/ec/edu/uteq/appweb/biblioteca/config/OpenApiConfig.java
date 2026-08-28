package ec.edu.uteq.appweb.biblioteca.config;

import org.springframework.context.annotation.Configuration;

/**
 * ============================================================================
 * TODO-U4-3: DOCUMENTACION OPENAPI 3 CON SWAGGER UI
 * ============================================================================
 *
 * La dependencia springdoc-openapi-starter-webmvc-ui 3.1.0 ya esta en el pom y
 * la ruta de la interfaz ya esta fijada en application.yml
 * (springdoc.swagger-ui.path: /api/docs).
 *
 * Falta que usted:
 *   1. Declare un @Bean OpenAPI con el titulo, la version y el contacto del equipo.
 *   2. Declare el esquema de seguridad bearerAuth de tipo HTTP, scheme "bearer",
 *      bearerFormat "JWT", y lo agregue como requisito de seguridad global.
 *   3. Anote cada endpoint con @Operation(summary, description) y @ApiResponses
 *      declarando los codigos 200, 201, 400, 401, 403 y 404 que realmente devuelve.
 *   4. Agrupe los controladores con @Tag.
 *
 * Criterio de verificacion de la Guia: Swagger UI accesible y con todos los
 * endpoints documentados con sus esquemas de peticion y respuesta.
 */
@Configuration
public class OpenApiConfig {

    // TODO-U4-3: @Bean public OpenAPI apiBiblioteca() Ellipsis
}
