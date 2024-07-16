package literature.repository;


import literature.model.autor.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepositorio extends JpaRepository<Autor, String> {

    boolean existsByName(String nombre);
    Autor getByName(String nombre);

    @Query("""
           SELECT a FROM Autor a WHERE CAST(a.fechaNacimiento AS INTEGER) <= :fecha AND CAST(a.fechaFallecimiento AS INTEGER) >= :fecha
           """)
    List<Autor> findAllAutoresAliveInYear(int fecha);

}
