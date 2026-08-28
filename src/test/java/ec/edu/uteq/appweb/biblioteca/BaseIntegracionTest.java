package ec.edu.uteq.appweb.biblioteca;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Base de las pruebas de integracion HTTP. YA IMPLEMENTADA: heredela y escriba
 * solo sus casos de prueba.
 *
 * Levanta un PostgreSQL 18 real en un contenedor efimero y deja que Flyway
 * aplique V1, V2 y V3 sobre el, de modo que cada ejecucion parte del mismo
 * estado conocido. @ServiceConnection sustituye a la vieja @DynamicPropertySource:
 * Spring Boot descubre solo la URL, el usuario y la clave del contenedor.
 *
 * Nota de version: en Testcontainers 2.x la clase vive en
 * org.testcontainers.postgresql.PostgreSQLContainer. La ruta antigua
 * org.testcontainers.containers.PostgreSQLContainer quedo obsoleta.
 *
 * Requisito: Docker debe estar corriendo en la maquina.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@Tag("integracion")
public abstract class BaseIntegracionTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:18-alpine")
                    .withDatabaseName("biblioteca_test")
                    .withUsername("test")
                    .withPassword("test");
}
