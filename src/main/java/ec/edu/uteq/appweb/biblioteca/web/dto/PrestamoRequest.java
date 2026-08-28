package ec.edu.uteq.appweb.biblioteca.web.dto;

import jakarta.validation.constraints.NotNull;

public record PrestamoRequest(

        @NotNull(message = "el libro es obligatorio")
        Long libroId,

        @NotNull(message = "el socio es obligatorio")
        Long socioId,

        @NotNull(message = "los dias de prestamo son obligatorios")
        Integer diasPrestamo) {
}
