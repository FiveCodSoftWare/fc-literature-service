package literature.model.autor;

import java.util.List;

public record ListaDatosAutor (
        String name,
        String fechaNacimiento,
        String fechaFallecimiento,
        List<String> libros
) {}