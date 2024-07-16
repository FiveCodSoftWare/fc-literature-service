package literature.model.libro;


public record ListaLibro (
        String titulo,
        String autor,
        String idioma,
        Integer descargas
) {
    public ListaLibro (DatosLibro datosLibro) {
        this(
                datosLibro.titulo(),
                datosLibro.autor().getFirst().nombre(),
                datosLibro.idioma().getFirst().split(",")[0],
                datosLibro.descargas()
        );
    }

    public ListaLibro (Libro libro) {
        this(libro.getTitulo(),libro.getAutor().getName(), libro.getIdioma(), libro.getDescargas());
    }
}