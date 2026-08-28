package ec.edu.uteq.appweb.biblioteca.web.dto;

import org.springframework.data.domain.Page;

/**
 * Metadatos de paginacion que viajan en el campo meta del envoltorio ApiResponse.
 */
public record PageMeta(int page, int size, long totalElements, int totalPages) {

    public static PageMeta de(Page<?> pagina) {
        return new PageMeta(pagina.getNumber(), pagina.getSize(),
                pagina.getTotalElements(), pagina.getTotalPages());
    }
}
