package ec.edu.uteq.appweb.biblioteca.web.dto;

import java.time.LocalDate;

public record PrestamoResponse(Long id,
                               String libroTitulo,
                               String libroIsbn,
                               String socioNombre,
                               LocalDate fechaPrestamo,
                               LocalDate fechaDevolucionPrevista,
                               LocalDate fechaDevolucionReal,
                               String estado) {
}
