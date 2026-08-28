package ec.edu.uteq.appweb.biblioteca.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AutorRequest(

        @NotBlank(message = "el nombre es obligatorio")
        @Size(max = 150, message = "el nombre admite maximo 150 caracteres")
        String nombre,

        @Size(max = 80, message = "la nacionalidad admite maximo 80 caracteres")
        String nacionalidad) {
}
