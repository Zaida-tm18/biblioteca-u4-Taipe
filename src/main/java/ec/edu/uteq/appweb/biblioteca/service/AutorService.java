package ec.edu.uteq.appweb.biblioteca.service;

import ec.edu.uteq.appweb.biblioteca.domain.Autor;
import ec.edu.uteq.appweb.biblioteca.exception.RecursoNoEncontradoException;
import ec.edu.uteq.appweb.biblioteca.repository.AutorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AutorService {

    private final AutorRepository repositorio;

    public AutorService(AutorRepository repositorio) {
        this.repositorio = repositorio;
    }

    public Page<Autor> listar(Pageable paginacion) {
        return repositorio.findAll(paginacion);
    }

    public Autor buscarPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> RecursoNoEncontradoException.de("Autor", id));
    }

    @Transactional
    public Autor crear(String nombre, String nacionalidad) {
        return repositorio.save(new Autor(nombre, nacionalidad));
    }

    @Transactional
    public Autor actualizar(Long id, String nombre, String nacionalidad) {
        Autor autor = buscarPorId(id);
        autor.setNombre(nombre);
        autor.setNacionalidad(nacionalidad);
        return repositorio.save(autor);
    }

    @Transactional
    public void eliminar(Long id) {
        Autor autor = buscarPorId(id);
        repositorio.delete(autor);
    }
}
