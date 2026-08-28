package ec.edu.uteq.appweb.biblioteca.web.mapper;

import ec.edu.uteq.appweb.biblioteca.domain.Autor;
import ec.edu.uteq.appweb.biblioteca.web.dto.AutorResponse;
import org.springframework.stereotype.Component;

@Component
public class AutorMapper {

    public AutorResponse aRespuesta(Autor autor) {
        return new AutorResponse(autor.getId(), autor.getNombre(), autor.getNacionalidad());
    }
}
