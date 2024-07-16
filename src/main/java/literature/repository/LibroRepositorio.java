package literature.repository;


import literature.model.libro.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String> {

    boolean existsByTitulo(String titulos);

    @Query("""
            SELECT l.titulo FROM Libro l WHERE l.autor.id=:id
            """)
    List<String> getAllLibrosByAutor(String id);

    Optional<Libro> findAllByIdioma(String idioma);

}
