package ec.edu.uteq.appweb.biblioteca.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SocioRequest(

        @NotBlank(message = "la cedula es obligatoria")
        @Pattern(regexp = "^[0-9]{10}$", message = "la cedula debe tener exactamente 10 digitos")
        String cedula,

        @NotBlank(message = "el nombre completo es obligatorio")
        @Size(max = 200, message = "el nombre admite maximo 200 caracteres")
        String nombreCompleto,

        @NotBlank(message = "el correo es obligatorio")
        @Email(message = "el correo no tiene un formato valido")
        String correo) {
}
