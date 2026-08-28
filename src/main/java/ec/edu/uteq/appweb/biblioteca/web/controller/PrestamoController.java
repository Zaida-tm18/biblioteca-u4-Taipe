package ec.edu.uteq.appweb.biblioteca.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO-U4-1: API REST de prestamos.
 *
 *   GET  /api/v1/prestamos?estado=ACTIVO   paginado con meta
 *   POST /api/v1/prestamos                 201 + Location, rol BIBLIOTECARIO o ADMIN
 *   POST /api/v1/prestamos/{id}/devolucion 200, rol BIBLIOTECARIO o ADMIN
 *
 * Observe que PrestamoService ya lanza ReglaNegocioException cuando el socio
 * supera los tres prestamos activos o cuando no hay ejemplares: eso debe salir
 * como 409 Conflict en formato ProblemDetail, y ya lo hace el manejador global.
 * No lo capture usted en el controlador.
 */
@RestController
@RequestMapping("/api/v1/prestamos")
public class PrestamoController {

    // TODO-U4-1
}
