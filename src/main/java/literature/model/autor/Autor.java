package literature.model.autor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import literature.model.libro.Libro;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "autores")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String fechaNacimiento;
    private String fechaFallecimiento;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Libro> libros;

    public Autor(DatosAutor datosAutor) {
        this.name = datosAutor.nombre();
        this.fechaNacimiento = datosAutor.fecha_nacimiento();
        this.fechaFallecimiento = datosAutor.fecha_fallecimiento();
        this.libros = new ArrayList<>();
    }

    public Autor() {
    }

    public Autor(RegistroDatosAutor registroDatosAutor) {
        this.name = registroDatosAutor.name();
        this.fechaNacimiento = registroDatosAutor.fechaNacimiento();
        this.fechaFallecimiento = registroDatosAutor.fechaFallecimiento();
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }
    public String getFechaFallecimiento() {
        return fechaFallecimiento;
    }
    public List<Libro> getLibros() {
        return libros;
    }
}
