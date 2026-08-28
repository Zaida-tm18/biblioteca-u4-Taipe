package ec.edu.uteq.appweb.biblioteca.web.dto;

import java.time.LocalDate;

public record SocioResponse(Long id,
                            String cedula,
                            String nombreCompleto,
                            String correo,
                            LocalDate fechaRegistro,
                            boolean activo) {
}
