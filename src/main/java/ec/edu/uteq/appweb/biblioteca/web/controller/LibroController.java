package ec.edu.uteq.appweb.biblioteca.web.controller;

import ec.edu.uteq.appweb.biblioteca.domain.Libro;
import ec.edu.uteq.appweb.biblioteca.service.LibroService;
import ec.edu.uteq.appweb.biblioteca.web.dto.ApiResponse;
import ec.edu.uteq.appweb.biblioteca.web.dto.LibroResponse;
import ec.edu.uteq.appweb.biblioteca.web.dto.PageMeta;
import ec.edu.uteq.appweb.biblioteca.web.mapper.LibroMapper;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ============================================================================
 * TODO-U4-1 (Objetivo especifico 2 de la Guia): API REST DEL CATALOGO
 * ============================================================================
 *
 * Replique el patron de AutorController, que ya esta implementado y comentado.
 * LibroService y LibroMapper estan completos: usted solo expone, no reimplementa.
 *
 * Endpoints exigidos:
 *   GET    /api/v1/libros                 [IMPLEMENTADO - B1]
 *   GET    /api/v1/libros/{id}            200 o 404 con ProblemDetail
 *   POST   /api/v1/libros                 201 + Location, rol ADMIN
 *   PUT    /api/v1/libros/{id}            200, rol ADMIN
 *   DELETE /api/v1/libros/{id}            204, rol ADMIN, borrado logico
 *   GET    /api/v1/libros/{id}/enriquecido combina el libro local con Open Library
 *                                         (depende del TODO-U4-4)
 *
 * Recuerde: exito en ApiResponse, error en ProblemDetail, nunca los dos mezclados.
 */
@RestController
@RequestMapping("/api/v1/libros")
public class LibroController {

    private final LibroService servicio;
    private final LibroMapper mapper;

    public LibroController(LibroService servicio, LibroMapper mapper) {
        this.servicio = servicio;
        this.mapper = mapper;
    }

    /**
     * B1 - Listado de libros con filtros, paginacion y envoltorio.
     *
     * URI REST: sustantivo en plural, versionada en /v1 y sin verbos. El filtrado
     * viaja como query params opcionales, no como segmentos de ruta.
     *
     * Los tres filtros se delegan tal cual a LibroService.buscar(...), que ya
     * combina las Specifications (solo activos + titulo + categoria + anio).
     * Si un parametro llega nulo, la Specification correspondiente se ignora.
     *
     * La paginacion la resuelve Spring con @PageableDefault(size = 20): el cliente
     * puede sobrescribirla con ?page=, ?size= y ?sort=.
     *
     * La respuesta es 200 con el envoltorio {success, data, message, errors, meta},
     * donde data es la lista de LibroResponse y meta son los PageMeta de la pagina.
     */
    @GetMapping
    public ApiResponse<List<LibroResponse>> listar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Integer anioDesde,
            @PageableDefault(size = 20) Pageable paginacion) {

        Page<Libro> pagina = servicio.buscar(titulo, categoriaId, anioDesde, paginacion);

        List<LibroResponse> datos = pagina.getContent().stream()
                .map(mapper::aRespuesta)
                .toList();

        return ApiResponse.ok(datos, "Libros listados", PageMeta.de(pagina));
    }

    // TODO-U4-1: implementar los endpoints restantes (detalle, creacion, actualizacion,
    // borrado logico y enriquecido) e inyectar OpenLibraryClient cuando toque el TODO-U4-4.
}