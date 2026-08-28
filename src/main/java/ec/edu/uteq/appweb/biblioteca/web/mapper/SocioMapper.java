package ec.edu.uteq.appweb.biblioteca.web.mapper;

import ec.edu.uteq.appweb.biblioteca.domain.Socio;
import ec.edu.uteq.appweb.biblioteca.web.dto.SocioResponse;
import org.springframework.stereotype.Component;

@Component
public class SocioMapper {

    public SocioResponse aRespuesta(Socio socio) {
        return new SocioResponse(
                socio.getId(),
                socio.getCedula(),
                socio.getNombreCompleto(),
                socio.getCorreo(),
                socio.getFechaRegistro(),
                socio.isActivo());
    }
}
