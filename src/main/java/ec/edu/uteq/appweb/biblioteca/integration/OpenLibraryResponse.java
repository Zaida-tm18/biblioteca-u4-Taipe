package ec.edu.uteq.appweb.biblioteca.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Proyeccion parcial de la respuesta de Open Library para un ISBN.
 * Solo se mapean los campos que interesan; el resto se ignora.
 *
 * Ejemplo real: https://openlibrary.org/isbn/9780134494166.json
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenLibraryResponse(String title,
                                  Integer number_of_pages,
                                  String publish_date,
                                  List<Integer> covers) {

    public String urlPortada() {
        if (covers == null || covers.isEmpty()) {
            return null;
        }
        return "https://covers.openlibrary.org/b/id/" + covers.get(0) + "-M.jpg";
    }
}
