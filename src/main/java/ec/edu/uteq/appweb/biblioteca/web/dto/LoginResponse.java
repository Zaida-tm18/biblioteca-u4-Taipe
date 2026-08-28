package ec.edu.uteq.appweb.biblioteca.web.dto;

public record LoginResponse(String username, String rol, String tokenType, long expiresInSeconds) {
}
