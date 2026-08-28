package ec.edu.uteq.appweb.biblioteca.service;

import ec.edu.uteq.appweb.biblioteca.domain.EstadoPrestamo;
import ec.edu.uteq.appweb.biblioteca.domain.Libro;
import ec.edu.uteq.appweb.biblioteca.domain.Prestamo;
import ec.edu.uteq.appweb.biblioteca.domain.Socio;
import ec.edu.uteq.appweb.biblioteca.exception.RecursoNoEncontradoException;
import ec.edu.uteq.appweb.biblioteca.exception.ReglaNegocioException;
import ec.edu.uteq.appweb.biblioteca.repository.LibroRepository;
import ec.edu.uteq.appweb.biblioteca.repository.PrestamoRepository;
import ec.edu.uteq.appweb.biblioteca.repository.SocioRepository;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Reglas de prestamo. COMPLETO: el limite de tres prestamos activos por socio
 * y el descuento de ejemplares ya estan implementados y probados.
 */
@Service
@Transactional(readOnly = true)
public class PrestamoService {

    public static final int MAXIMO_PRESTAMOS_ACTIVOS = 3;

    private final PrestamoRepository prestamos;
    private final LibroRepository libros;
    private final SocioRepository socios;

    public PrestamoService(PrestamoRepository prestamos, LibroRepository libros, SocioRepository socios) {
        this.prestamos = prestamos;
        this.libros = libros;
        this.socios = socios;
    }

    public Page<Prestamo> listarPorEstado(EstadoPrestamo estado, Pageable paginacion) {
        return prestamos.findByEstado(estado, paginacion);
    }

    public Prestamo buscarPorId(Long id) {
        return prestamos.findById(id)
                .orElseThrow(() -> RecursoNoEncontradoException.de("Prestamo", id));
    }

    @Transactional
    public Prestamo registrar(Long libroId, Long socioId, int diasPrestamo) {
        Libro libro = libros.findById(libroId)
                .orElseThrow(() -> RecursoNoEncontradoException.de("Libro", libroId));
        Socio socio = socios.findById(socioId)
                .orElseThrow(() -> RecursoNoEncontradoException.de("Socio", socioId));

        if (!socio.isActivo()) {
            throw new ReglaNegocioException("El socio " + socio.getCedula() + " esta inactivo");
        }
        long activos = prestamos.countBySocioIdAndEstado(socioId, EstadoPrestamo.ACTIVO);
        if (activos >= MAXIMO_PRESTAMOS_ACTIVOS) {
            throw new ReglaNegocioException(
                    "El socio ya tiene " + activos + " prestamos activos; el maximo permitido es "
                            + MAXIMO_PRESTAMOS_ACTIVOS);
        }
        if (libro.getEjemplaresDisponibles() <= 0) {
            throw new ReglaNegocioException("No hay ejemplares disponibles del libro " + libro.getIsbn());
        }

        libro.prestarEjemplar();
        libros.save(libro);
        return prestamos.save(new Prestamo(libro, socio, LocalDate.now().plusDays(diasPrestamo)));
    }

    @Transactional
    public Prestamo devolver(Long prestamoId) {
        Prestamo prestamo = buscarPorId(prestamoId);
        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new ReglaNegocioException("El prestamo " + prestamoId + " ya fue devuelto");
        }
        prestamo.registrarDevolucion(LocalDate.now());
        Libro libro = prestamo.getLibro();
        libro.devolverEjemplar();
        libros.save(libro);
        return prestamos.save(prestamo);
    }
}
