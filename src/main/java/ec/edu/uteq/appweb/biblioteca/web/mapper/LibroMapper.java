package ec.edu.uteq.appweb.biblioteca.web.mapper;

import ec.edu.uteq.appweb.biblioteca.domain.Libro;
import ec.edu.uteq.appweb.biblioteca.web.dto.LibroResponse;
import org.springframework.stereotype.Component;

@Component
public class LibroMapper {

    public LibroResponse aRespuesta(Libro libro) {
        return new LibroResponse(
                libro.getId(),
                libro.getIsbn(),
                libro.getTitulo(),
                libro.getAnioPublicacion(),
                libro.getEjemplaresTotales(),
                libro.getEjemplaresDisponibles(),
                libro.getAutor().getNombre(),
                libro.getEditorial().getNombre(),
                libro.getCategoria().getNombre());
    }
}
