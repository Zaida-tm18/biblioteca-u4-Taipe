package ec.edu.uteq.appweb.biblioteca.service;

import ec.edu.uteq.appweb.biblioteca.domain.Libro;
import ec.edu.uteq.appweb.biblioteca.exception.RecursoNoEncontradoException;
import ec.edu.uteq.appweb.biblioteca.exception.ReglaNegocioException;
import ec.edu.uteq.appweb.biblioteca.repository.AutorRepository;
import ec.edu.uteq.appweb.biblioteca.repository.CategoriaRepository;
import ec.edu.uteq.appweb.biblioteca.repository.EditorialRepository;
import ec.edu.uteq.appweb.biblioteca.repository.LibroRepository;
import ec.edu.uteq.appweb.biblioteca.repository.LibroSpecs;
import ec.edu.uteq.appweb.biblioteca.web.dto.LibroRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Logica de negocio del catalogo. Corresponde a las Unidades I-III y esta COMPLETA:
 * el trabajo de la Unidad IV es exponerla por HTTP, no reescribirla.
 */
@Service
@Transactional(readOnly = true)
public class LibroService {

    private final LibroRepository libros;
    private final AutorRepository autores;
    private final EditorialRepository editoriales;
    private final CategoriaRepository categorias;

    public LibroService(LibroRepository libros,
                        AutorRepository autores,
                        EditorialRepository editoriales,
                        CategoriaRepository categorias) {
        this.libros = libros;
        this.autores = autores;
        this.editoriales = editoriales;
        this.categorias = categorias;
    }

    public Page<Libro> listarActivos(Pageable paginacion) {
        return libros.findByActivoTrue(paginacion);
    }

    public Page<Libro> buscar(String titulo, Long categoriaId, Integer anioDesde, Pageable paginacion) {
        Specification<Libro> criterio = Specification.allOf(
                LibroSpecs.soloActivos(),
                LibroSpecs.tituloContiene(titulo),
                LibroSpecs.deCategoria(categoriaId),
                LibroSpecs.publicadoDesde(anioDesde));
        return libros.findAll(criterio, paginacion);
    }

    public Libro buscarPorId(Long id) {
        return libros.findById(id)
                .orElseThrow(() -> RecursoNoEncontradoException.de("Libro", id));
    }

    public Libro buscarPorIsbn(String isbn) {
        return libros.findByIsbn(isbn)
                .orElseThrow(() -> new RecursoNoEncontradoException("Libro con ISBN " + isbn + " no existe"));
    }

    @Transactional
    public Libro crear(LibroRequest solicitud) {
        if (libros.existsByIsbn(solicitud.isbn())) {
            throw new ReglaNegocioException("Ya existe un libro registrado con el ISBN " + solicitud.isbn());
        }
        Libro libro = new Libro(
                solicitud.isbn(),
                solicitud.titulo(),
                solicitud.anioPublicacion(),
                solicitud.ejemplaresTotales(),
                autores.findById(solicitud.autorId())
                        .orElseThrow(() -> RecursoNoEncontradoException.de("Autor", solicitud.autorId())),
                editoriales.findById(solicitud.editorialId())
                        .orElseThrow(() -> RecursoNoEncontradoException.de("Editorial", solicitud.editorialId())),
                categorias.findById(solicitud.categoriaId())
                        .orElseThrow(() -> RecursoNoEncontradoException.de("Categoria", solicitud.categoriaId())));
        return libros.save(libro);
    }

    @Transactional
    public Libro actualizar(Long id, LibroRequest solicitud) {
        Libro libro = buscarPorId(id);
        libro.setTitulo(solicitud.titulo());
        libro.setAnioPublicacion(solicitud.anioPublicacion());
        libro.setEjemplaresTotales(solicitud.ejemplaresTotales());
        libro.setAutor(autores.findById(solicitud.autorId())
                .orElseThrow(() -> RecursoNoEncontradoException.de("Autor", solicitud.autorId())));
        libro.setEditorial(editoriales.findById(solicitud.editorialId())
                .orElseThrow(() -> RecursoNoEncontradoException.de("Editorial", solicitud.editorialId())));
        libro.setCategoria(categorias.findById(solicitud.categoriaId())
                .orElseThrow(() -> RecursoNoEncontradoException.de("Categoria", solicitud.categoriaId())));
        return libros.save(libro);
    }

    /**
     * Borrado logico: la Unidad III fijo que el catalogo no elimina filas.
     */
    @Transactional
    public void desactivar(Long id) {
        Libro libro = buscarPorId(id);
        libro.setActivo(false);
        libros.save(libro);
    }
}
