package ec.edu.uteq.appweb.biblioteca.exception;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejo uniforme de errores segun RFC 9457 (Problem Details for HTTP APIs),
 * que obsoleta a la RFC 7807. Implementado en la Unidad III: NO es parte del
 * trabajo de la Unidad IV, pero si es la referencia de como deben salir los errores.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String BASE_TIPO = "https://uteq.edu.ec/errores/";

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ProblemDetail noEncontrado(RecursoNoEncontradoException ex) {
        return construir(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage(), "no-encontrado");
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ProblemDetail reglaNegocio(ReglaNegocioException ex) {
        return construir(HttpStatus.CONFLICT, "Regla de negocio incumplida", ex.getMessage(), "regla-negocio");
    }

    @ExceptionHandler(ServicioExternoException.class)
    public ProblemDetail servicioExterno(ServicioExternoException ex) {
        return construir(HttpStatus.BAD_GATEWAY, "Servicio externo no disponible", ex.getMessage(), "servicio-externo");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail accesoDenegado(AccessDeniedException ex) {
        return construir(HttpStatus.FORBIDDEN, "Acceso denegado",
                "No tiene permisos suficientes para ejecutar esta operacion", "acceso-denegado");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validacion(MethodArgumentNotValidException ex) {
        ProblemDetail problema = construir(HttpStatus.BAD_REQUEST, "Solicitud invalida",
                "Uno o mas campos no superaron la validacion", "validacion");
        List<String> errores = new ArrayList<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errores.add(error.getField() + ": " + error.getDefaultMessage()));
        problema.setProperty("errors", errores);
        return problema;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail generico(Exception ex) {
        return construir(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno",
                "Ocurrio un error inesperado al procesar la solicitud", "interno");
    }

    private ProblemDetail construir(HttpStatus estado, String titulo, String detalle, String tipo) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(estado, detalle);
        problema.setTitle(titulo);
        problema.setType(URI.create(BASE_TIPO + tipo));
        problema.setProperty("timestamp", OffsetDateTime.now().toString());
        return problema;
    }
}
