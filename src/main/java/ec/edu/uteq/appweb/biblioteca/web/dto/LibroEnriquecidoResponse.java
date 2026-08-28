package ec.edu.uteq.appweb.biblioteca.web.dto;

/**
 * Respuesta del endpoint que combina el libro local con los datos traidos
 * de la API externa. Los cuatro campos externos pueden venir nulos si el
 * ISBN no existe en el proveedor o si el servicio externo esta caido.
 */
public record LibroEnriquecidoResponse(LibroResponse libro,
                                       String tituloExterno,
                                       String urlPortada,
                                       Integer numeroPaginas,
                                       String fechaPublicacionExterna) {
}
