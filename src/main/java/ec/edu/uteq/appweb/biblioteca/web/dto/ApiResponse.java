package ec.edu.uteq.appweb.biblioteca.web.dto;

import java.util.List;

/**
 * Envoltorio uniforme exigido por el objetivo especifico 2 de la Guia PE-U4:
 * {success, data, message, errors, meta}.
 *
 * Se usa UNICAMENTE en respuestas exitosas. Los errores viajan como ProblemDetail
 * (RFC 9457), producido por GlobalExceptionHandler. No mezclar ambos formatos.
 */
public record ApiResponse<T>(boolean success, T data, String message, List<String> errors, Object meta) {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, "Operacion exitosa", List.of(), null);
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>(true, data, message, List.of(), null);
    }

    public static <T> ApiResponse<T> ok(T data, String message, Object meta) {
        return new ApiResponse<>(true, data, message, List.of(), meta);
    }
}
