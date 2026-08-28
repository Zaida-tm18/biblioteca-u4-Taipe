package ec.edu.uteq.appweb.biblioteca.integration;

import ec.edu.uteq.appweb.biblioteca.config.CacheConfig;
import ec.edu.uteq.appweb.biblioteca.exception.ServicioExternoException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * ============================================================================
 * TODO-U4-4 (Objetivo especifico 3 de la Guia): CONSUMO DE API EXTERNA
 * ============================================================================
 * [IMPLEMENTADO - B3]
 *
 * El consumo se hace desde el servidor con el bean RestClient de RestClientConfig,
 * que ya trae baseUrl y los timeouts de conexion y lectura puestos. No se crea
 * aqui ningun cliente nuevo: eso perderia los timeouts y reabriria el riesgo de
 * fallo en cascada (Nygard, Release It!).
 */
@Component
public class OpenLibraryClient {

    private final RestClient restClient;

    public OpenLibraryClient(RestClient restClientExterno) {
        this.restClient = restClientExterno;
    }

    /**
     * Consulta los metadatos de un ISBN en Open Library.
     *
     * CACHE-ASIDE sobre CacheConfig.CACHE_OPENLIBRARY (TTL de 24 horas):
     * @Cacheable es la forma declarativa del patron. Spring mira primero el
     * namespace "openlibrary"; si hay acierto devuelve el valor y este metodo
     * ni siquiera se ejecuta; si hay fallo ejecuta el metodo y guarda el
     * resultado. La clave es el propio ISBN, de modo que en Redis queda como
     * "openlibrary::9780134494166" y la verificacion
     *   docker compose exec redis redis-cli KEYS "openlibrary*"
     * la encuentra tras la primera llamada.
     *
     * El TTL de 24 h se justifica por la volatilidad del dato: titulo, numero de
     * paginas, fecha de publicacion y portada de un ISBN ya publicado son
     * practicamente inmutables, asi que una ventana larga maximiza los aciertos
     * sin riesgo real de servir datos obsoletos.
     *
     * NO SE CACHEAN LOS FALLOS, por dos vias complementarias:
     *   - unless = "#result == null" evita guardar el 404 del proveedor, que si
     *     entra por el camino feliz y devuelve null.
     *   - las excepciones (5xx, 4xx != 404, timeout) abortan el metodo, y
     *     @Cacheable nunca escribe cuando el metodo lanza. Ademas la
     *     configuracion de cache tiene disableCachingNullValues().
     * Si se cachearan los fallos, una caida momentanea del proveedor quedaria
     * congelada 24 horas.
     *
     * MANEJO DIFERENCIADO DE FALLOS: se usa exchange en vez de retrieve porque
     * asi se decide el tratamiento ANTES de intentar deserializar el cuerpo.
     * Con retrieve + onStatus vacio, Spring seguiria adelante e intentaria
     * convertir el cuerpo de error de Open Library (que en el 404 no es JSON
     * valido para este record) y el 404 acabaria disfrazado de 502.
     */
    @Cacheable(cacheNames = CacheConfig.CACHE_OPENLIBRARY,
               key = "#isbn",
               unless = "#result == null")
    public OpenLibraryResponse consultarPorIsbn(String isbn) {
        try {
            return restClient.get()
                    .uri("/isbn/{isbn}.json", isbn)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange((peticion, respuesta) -> {
                        HttpStatusCode estado = respuesta.getStatusCode();

                        // 404: el ISBN no existe en el proveedor. NO es un fallo de
                        // nuestro sistema: se devuelve vacio y el endpoint responde
                        // 200 con el libro local y los campos externos en null.
                        if (estado.value() == 404) {
                            return null;
                        }

                        // 5xx y cualquier 4xx distinto de 404: fallo del proveedor.
                        if (estado.isError()) {
                            throw new ServicioExternoException(
                                    "Open Library respondio " + estado.value()
                                            + " al consultar el ISBN " + isbn);
                        }

                        return respuesta.bodyTo(OpenLibraryResponse.class);
                    });

        } catch (ServicioExternoException ex) {
            // Ya viene clasificada desde el exchange: se deja subir tal cual.
            throw ex;
        } catch (ResourceAccessException ex) {
            // Timeout de conexion o de lectura, DNS caido, conexion rechazada.
            throw new ServicioExternoException(
                    "Open Library no respondio dentro del tiempo de espera para el ISBN " + isbn, ex);
        } catch (RestClientException ex) {
            // Cuerpo ilegible, content-type inesperado, error de conversion.
            throw new ServicioExternoException(
                    "Respuesta no procesable de Open Library para el ISBN " + isbn, ex);
        }
    }
}