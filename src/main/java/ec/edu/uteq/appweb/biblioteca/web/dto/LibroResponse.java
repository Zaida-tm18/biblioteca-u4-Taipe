package ec.edu.uteq.appweb.biblioteca.web.dto;

public record LibroResponse(Long id,
                            String isbn,
                            String titulo,
                            Integer anioPublicacion,
                            Integer ejemplaresTotales,
                            Integer ejemplaresDisponibles,
                            String autor,
                            String editorial,
                            String categoria) {
}
