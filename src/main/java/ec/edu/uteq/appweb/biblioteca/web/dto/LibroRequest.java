package ec.edu.uteq.appweb.biblioteca.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LibroRequest(

        @NotBlank(message = "el isbn es obligatorio")
        @Pattern(regexp = "^[0-9Xx-]{10,20}$", message = "el isbn admite solo digitos, guiones y X")
        String isbn,

        @NotBlank(message = "el titulo es obligatorio")
        @Size(max = 250, message = "el titulo admite maximo 250 caracteres")
        String titulo,

        @Min(value = 1450, message = "el anio de publicacion debe ser 1450 o posterior")
        Integer anioPublicacion,

        @NotNull(message = "los ejemplares totales son obligatorios")
        @Min(value = 1, message = "debe registrar al menos un ejemplar")
        Integer ejemplaresTotales,

        @NotNull(message = "el autor es obligatorio")
        Long autorId,

        @NotNull(message = "la editorial es obligatoria")
        Long editorialId,

        @NotNull(message = "la categoria es obligatoria")
        Long categoriaId) {
}
