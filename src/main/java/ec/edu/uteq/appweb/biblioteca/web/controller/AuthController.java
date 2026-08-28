package ec.edu.uteq.appweb.biblioteca.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO-U4-2: autenticacion.
 *
 *   POST /api/v1/auth/login   recibe LoginRequest, valida con BCrypt contra
 *                             UsuarioRepository.findByUsernameAndActivoTrue,
 *                             y devuelve LoginResponse dentro de ApiResponse.
 *                             El token va en la cabecera Authorization de las
 *                             siguientes peticiones o en una cookie HttpOnly.
 *   POST /api/v1/auth/logout  invalida el token por su jti (opcional, suma en la rubrica).
 *
 * Credenciales sembradas por Flyway en V3__usuarios.sql:
 *   admin / Admin123!          rol ADMIN
 *   bibliotecario / Biblio123! rol BIBLIOTECARIO
 *   lector / Lector123!        rol LECTOR
 *
 * Un login fallido debe devolver 401, no 200 con success=false.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    // TODO-U4-2
}
