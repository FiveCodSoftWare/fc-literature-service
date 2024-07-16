package literature.model.autor;

import java.util.List;

public record RegistroDatosAutor (
        String name,
        String fechaNacimiento,
        String fechaFallecimiento
) {
    public RegistroDatosAutor (List<DatosAutor> autor) {
        this(
                autor.getFirst().nombre(),
                autor.getFirst().fecha_nacimiento(),
                autor.getFirst().fecha_fallecimiento()
        );
    }
}