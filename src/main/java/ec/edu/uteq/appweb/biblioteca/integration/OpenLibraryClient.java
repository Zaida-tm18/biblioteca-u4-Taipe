package ec.edu.uteq.appweb.biblioteca.integration;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * ============================================================================
 * TODO-U4-4 (Objetivo especifico 3 de la Guia): CONSUMO DE API EXTERNA
 * ============================================================================
 *
 * El bean RestClient ya viene configurado con baseUrl y timeouts
 * (ver RestClientConfig). Usted debe implementar consultarPorIsbn con:
 *
 *   1. Cache-aside en Redis sobre el namespace CacheConfig.CACHE_OPENLIBRARY,
 *      cuyo TTL de 24 horas ya esta definido. La anotacion @Cacheable basta;
 *      justifique el TTL en el informe segun la volatilidad del dato.
 *   2. Manejo diferenciado de fallos, que es lo que realmente se evalua:
 *        - 404 del proveedor  -> devolver vacio, NO es un error de su sistema.
 *        - 4xx distinto de 404 -> ServicioExternoException.
 *        - 5xx                 -> ServicioExternoException.
 *        - timeout o fallo de red -> ServicioExternoException.
 *      GlobalExceptionHandler ya convierte ServicioExternoException en un
 *      ProblemDetail 502 Bad Gateway, asi que no escriba respuestas aqui.
 *   3. NUNCA cachear un fallo: use unless o condition para evitarlo.
 *
 * Pista con RestClient:
 *   restClient.get()
 *       .uri("/isbn/{isbn}.json", isbn)
 *       .retrieve()
 *       .onStatus(estado -> estado.value() == 404, (peticion, respuesta) -> { })
 *       .body(OpenLibraryResponse.class);
 *
 * Evidencia que pide la Guia: capture la clave cacheada con
 *   docker compose exec redis redis-cli KEYS "openlibrary*"
 */
@Component
public class OpenLibraryClient {

    private final RestClient restClient;

    public OpenLibraryClient(RestClient restClientExterno) {
        this.restClient = restClientExterno;
    }

    public OpenLibraryResponse consultarPorIsbn(String isbn) {
        throw new UnsupportedOperationException("TODO-U4-4: consumir Open Library con cache y manejo de errores");
    }
}
