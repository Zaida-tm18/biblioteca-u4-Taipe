package ec.edu.uteq.appweb.biblioteca.service;

import ec.edu.uteq.appweb.biblioteca.domain.Socio;
import ec.edu.uteq.appweb.biblioteca.exception.RecursoNoEncontradoException;
import ec.edu.uteq.appweb.biblioteca.exception.ReglaNegocioException;
import ec.edu.uteq.appweb.biblioteca.repository.SocioRepository;
import ec.edu.uteq.appweb.biblioteca.web.dto.SocioRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SocioService {

    private final SocioRepository socios;

    public SocioService(SocioRepository socios) {
        this.socios = socios;
    }

    public Page<Socio> listarActivos(Pageable paginacion) {
        return socios.findByActivoTrue(paginacion);
    }

    public Socio buscarPorId(Long id) {
        return socios.findById(id)
                .orElseThrow(() -> RecursoNoEncontradoException.de("Socio", id));
    }

    @Transactional
    public Socio crear(SocioRequest solicitud) {
        if (socios.existsByCedula(solicitud.cedula())) {
            throw new ReglaNegocioException("Ya existe un socio con la cedula " + solicitud.cedula());
        }
        if (socios.existsByCorreoIgnoreCase(solicitud.correo())) {
            throw new ReglaNegocioException("Ya existe un socio con el correo " + solicitud.correo());
        }
        return socios.save(new Socio(solicitud.cedula(), solicitud.nombreCompleto(), solicitud.correo()));
    }

    @Transactional
    public Socio actualizar(Long id, SocioRequest solicitud) {
        Socio socio = buscarPorId(id);
        socio.setNombreCompleto(solicitud.nombreCompleto());
        socio.setCorreo(solicitud.correo());
        return socios.save(socio);
    }

    @Transactional
    public void desactivar(Long id) {
        Socio socio = buscarPorId(id);
        socio.setActivo(false);
        socios.save(socio);
    }
}
