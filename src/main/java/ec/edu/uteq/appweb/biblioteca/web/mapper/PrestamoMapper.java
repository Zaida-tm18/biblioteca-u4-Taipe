package ec.edu.uteq.appweb.biblioteca.web.mapper;

import ec.edu.uteq.appweb.biblioteca.domain.Prestamo;
import ec.edu.uteq.appweb.biblioteca.web.dto.PrestamoResponse;
import org.springframework.stereotype.Component;

@Component
public class PrestamoMapper {

    public PrestamoResponse aRespuesta(Prestamo prestamo) {
        return new PrestamoResponse(
                prestamo.getId(),
                prestamo.getLibro().getTitulo(),
                prestamo.getLibro().getIsbn(),
                prestamo.getSocio().getNombreCompleto(),
                prestamo.getFechaPrestamo(),
                prestamo.getFechaDevolucionPrevista(),
                prestamo.getFechaDevolucionReal(),
                prestamo.getEstado().name());
    }
}
