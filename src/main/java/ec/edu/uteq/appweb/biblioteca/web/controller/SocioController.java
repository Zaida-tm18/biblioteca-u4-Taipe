package ec.edu.uteq.appweb.biblioteca.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO-U4-1: API REST de socios. Mismo patron que AutorController.
 *
 *   GET    /api/v1/socios        paginado con meta
 *   GET    /api/v1/socios/{id}
 *   POST   /api/v1/socios        201 + Location, rol ADMIN o BIBLIOTECARIO
 *   PUT    /api/v1/socios/{id}   rol ADMIN o BIBLIOTECARIO
 *   DELETE /api/v1/socios/{id}   204, rol ADMIN, desactivacion logica
 */
@RestController
@RequestMapping("/api/v1/socios")
public class SocioController {

    // TODO-U4-1
}
