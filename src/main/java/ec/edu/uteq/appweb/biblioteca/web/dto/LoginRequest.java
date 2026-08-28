package ec.edu.uteq.appweb.biblioteca.web.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "el usuario es obligatorio")
        String username,

        @NotBlank(message = "la contrasena es obligatoria")
        String password) {
}
