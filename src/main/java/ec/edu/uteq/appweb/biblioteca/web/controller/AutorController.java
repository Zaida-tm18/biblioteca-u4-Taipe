package ec.edu.uteq.appweb.biblioteca.web.controller;

import ec.edu.uteq.appweb.biblioteca.domain.Autor;
import ec.edu.uteq.appweb.biblioteca.service.AutorService;
import ec.edu.uteq.appweb.biblioteca.web.dto.ApiResponse;
import ec.edu.uteq.appweb.biblioteca.web.dto.AutorRequest;
import ec.edu.uteq.appweb.biblioteca.web.dto.AutorResponse;
import ec.edu.uteq.appweb.biblioteca.web.dto.PageMeta;
import ec.edu.uteq.appweb.biblioteca.web.mapper.AutorMapper;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ============================================================================
 * CONTROLADOR DE REFERENCIA - YA IMPLEMENTADO - NO ES PARTE DE SU TAREA
 * ============================================================================
 *
 * Este controlador existe para que usted vea, sobre codigo que compila, el
 * patron exacto que debe replicar en LibroController, SocioController y
 * PrestamoController. Observe siete cosas:
 *
 *  1. La ruta base esta versionada: /api/v1/autores. Sustantivo en plural,
 *     sin verbos (Fielding, interfaz uniforme).
 *  2. Toda respuesta exitosa se envuelve en ApiResponse; los errores NO pasan
 *     por aqui: los produce GlobalExceptionHandler como ProblemDetail.
 *  3. El listado es paginado y publica sus metadatos en el campo meta.
 *  4. La creacion devuelve 201 Created con cabecera Location apuntando al
 *     recurso creado, no 200.
 *  5. La eliminacion devuelve 204 No Content sin cuerpo.
 *  6. Las escrituras estan protegidas por rol con @PreAuthorize.
 *  7. La validacion se declara con @Valid sobre el record de entrada; el
 *     manejador global la convierte en un 400 con el arreglo errors poblado.
 */
@RestController
@RequestMapping("/api/v1/autores")
public class AutorController {

    private final AutorService servicio;
    private final AutorMapper mapper;

    public AutorController(AutorService servicio, AutorMapper mapper) {
        this.servicio = servicio;
        this.mapper = mapper;
    }

    @GetMapping
    public ApiResponse<List<AutorResponse>> listar(@PageableDefault(size = 20) Pageable paginacion) {
        Page<Autor> pagina = servicio.listar(paginacion);
        List<AutorResponse> datos = pagina.getContent().stream().map(mapper::aRespuesta).toList();
        return ApiResponse.ok(datos, "Autores listados", PageMeta.de(pagina));
    }

    @GetMapping("/{id}")
    public ApiResponse<AutorResponse> buscar(@PathVariable Long id) {
        return ApiResponse.ok(mapper.aRespuesta(servicio.buscarPorId(id)), "Autor encontrado");
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AutorResponse>> crear(@Valid @RequestBody AutorRequest solicitud) {
        Autor creado = servicio.crear(solicitud.nombre(), solicitud.nacionalidad());
        AutorResponse cuerpo = mapper.aRespuesta(creado);
        return ResponseEntity
                .created(URI.create("/api/v1/autores/" + creado.getId()))
                .body(ApiResponse.ok(cuerpo, "Autor creado"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<AutorResponse> actualizar(@PathVariable Long id,
                                                 @Valid @RequestBody AutorRequest solicitud) {
        Autor actualizado = servicio.actualizar(id, solicitud.nombre(), solicitud.nacionalidad());
        return ApiResponse.ok(mapper.aRespuesta(actualizado), "Autor actualizado");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
