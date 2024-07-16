package literature.principal;

import jakarta.transaction.Transactional;
import literature.model.autor.Autor;
import literature.model.autor.DatosAutor;
import literature.model.autor.ListaDatosAutor;
import literature.model.autor.RegistroDatosAutor;
import literature.model.libro.DatosLibro;
import literature.model.libro.Libro;
import literature.model.libro.ListaLibro;
import literature.repository.AutorRepositorio;
import literature.repository.LibroRepositorio;
import literature.service.CambiarDatos;
import literature.service.ConsultarApiServicio;
import literature.utils.UtilsDatos;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class AppPrincipal {

    private final String url = "https://gutendex.com/books/";
    private final Scanner scanner = new Scanner(System.in);
    private final ConsultarApiServicio consultaApi = new ConsultarApiServicio();
    private final CambiarDatos cambiarDatos = new CambiarDatos();
    private final LibroRepositorio libroRepositorio;
    private final AutorRepositorio autorRepositorio;


    public AppPrincipal(AutorRepositorio autorRepositorio, LibroRepositorio libroRepositorio) {
        this.libroRepositorio = libroRepositorio;
        this.autorRepositorio = autorRepositorio;
    }

    @Transactional
    private void buscarLibro() throws Exception {
        System.out.println("Ingrese el nombre del libro:");
        String libro = scanner.nextLine();

        if (libro == null || libro.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar un nombre de libro válido");
        }

        String encode = URLEncoder.encode(libro, StandardCharsets.UTF_8);
        var requested = consultaApi.requestData(url + "?search=" + encode);
        UtilsDatos obteneredDatos = cambiarDatos.obtenerDatos(requested, UtilsDatos.class);

        obteneredDatos.libroList().stream().findFirst().ifPresentOrElse(
                datosLibro -> procesarLibroEncontrado(datosLibro, encode),
                () -> System.out.println("No se encontró ningún libro con el nombre: " + encode)
        );
    }

    private void procesarLibroEncontrado(DatosLibro datosLibro, String encode) {
        ListaLibro listaLibro = new ListaLibro(datosLibro);
        imprimirResultados(listaLibro, encode);

        if (!libroRepositorio.existsByTitulo(listaLibro.titulo())) {
            guardarLibroYAutor(listaLibro);
        } else {
            System.out.println(listaLibro.titulo() + " ya está registrado en la base de datos.");
        }
    }

    private void imprimirResultados(ListaLibro listaLibro, String encode) {
        System.out.println("RESULTADOS PARA: " + encode);
        System.out.println("Título    : " + listaLibro.titulo());
        System.out.println("Autor     : " + listaLibro.autor());
        System.out.println("Idioma    : " + listaLibro.idioma());
        System.out.println("Descargas : " + listaLibro.descargas());
    }

    private void guardarLibroYAutor(ListaLibro listaLibro) {
        Autor autor;
        if (!autorRepositorio.existsByName(listaLibro.autor())) {

            List<DatosAutor> datosAutor = obtenerDatosAutor(listaLibro);
            var datosDeAutor = new RegistroDatosAutor(datosAutor);
            autor = new Autor(datosDeAutor);
            autor = autorRepositorio.save(autor);
        } else {
            autor = autorRepositorio.getByName(listaLibro.autor());
        }

        libroRepositorio.save(new Libro(listaLibro, autor));
        System.out.println("Libro y autor guardados con éxito.");
    }

    private List<DatosAutor> obtenerDatosAutor(ListaLibro listaLibro) {

        DatosAutor datosAutor = new DatosAutor( listaLibro.autor(), null,null   );
        return List.of(datosAutor);
    }

    public void listarLibrosRegistrados() {
        List<ListaLibro> libros = libroRepositorio.findAll().stream()
                .map(ListaLibro::new)
                .toList();

        System.out.println("\nLISTA DE LIBROS REGISTRADOS\n");
        libros.forEach(this::imprimirLibro);
    }

    public void listarAutoresRegistrados() {
        List<ListaDatosAutor> autores = autorRepositorio.findAll().stream()
                .map(this::mapearAListaDatosAutor)
                .toList();

        System.out.println("\nLISTA DE AUTORES REGISTRADOS\n");
        autores.forEach(this::imprimirAutor);
    }

    public void listarAutoresVivosEnUnAnioDetermiando() {
        System.out.println("Ingrese el año para determinar los autores que estuvieron vivos:");
        int year = obtenerEntradaNumerica();

        List<ListaDatosAutor> autores = autorRepositorio.findAllAutoresAliveInYear(year).stream()
                .map(this::mapearAListaDatosAutor)
                .toList();

        System.out.printf("\nAUTORES VIVOS EN EL AÑO %d\n\n", year);
        autores.forEach(this::imprimirAutor);
    }

    private void listarLibrosPorIdioma() {
        System.out.println("Seleccione el idioma de los libros a buscar (es, en, pt, fr):");
        String idioma = scanner.nextLine().toLowerCase();

        List<ListaLibro> libros = libroRepositorio.findAllByIdioma(idioma).stream()
                .map(l -> new ListaLibro(l.getTitulo(), l.getAutor().getName(), l.getIdioma(), l.getDescargas()))
                .toList();

        if (libros.isEmpty()) {
            System.out.printf("No se encontró ningún libro del idioma \"%s\" registrado en la BD\n", idioma);
        } else {
            System.out.printf("\nLIBROS EN %s\n\n", idioma.toUpperCase());
            libros.forEach(this::imprimirLibro);
        }
    }

    public void mostrarMenu() {
        while (true) {
            imprimirMenu();
            int opcion = obtenerEntradaNumerica();

            try {
                switch (opcion) {
                    case 0 -> {
                        System.out.println("Cerrando la aplicación...");
                        return;
                    }
                    case 1 -> buscarLibro();
                    case 2 -> listarLibrosRegistrados();
                    case 3 -> listarAutoresRegistrados();
                    case 4 -> listarAutoresVivosEnUnAnioDetermiando();
                    case 5 -> listarLibrosPorIdioma();
                    default -> System.out.println("Opción inválida. Por favor, elija una opción válida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void imprimirMenu() {
        System.out.println("""         
            #########  INGRESE UNA OPCION  #########
            
            1 -> Buscar Libro
            2 -> Listar Libros Registrados
            3 -> Listar Autores Registrados
            4 -> Listar Autores Vivos En Un Determinado Año
            5 -> Listar Libros por idioma
            
            0 -> Cerrar el programa
           """);
    }

    private void imprimirLibro(ListaLibro l) {
        System.out.printf("""
            ------- LIBRO -------
            Título    : %s
            Autor     : %s
            Idioma    : %s
            Descargas : %d
            """, l.titulo(), l.autor(), l.idioma(), l.descargas());
    }

    private void imprimirAutor(ListaDatosAutor a) {
        System.out.printf("""
            ------- AUTOR -------
            Autor                  : %s
            Fecha de Nacimiento    : %s
            Fecha de Fallecimiento : %s
            Libros                 : %s
            """, a.name(), a.fechaNacimiento(), a.fechaFallecimiento(), a.libros());
    }

    private ListaDatosAutor mapearAListaDatosAutor(Autor a) {
        return new ListaDatosAutor(
                a.getName(),
                a.getFechaNacimiento(),
                a.getFechaFallecimiento(),
                libroRepositorio.getAllLibrosByAutor(a.getId())
        );
    }

    private int obtenerEntradaNumerica() {
        while (!scanner.hasNextInt()) {
            System.out.println("Por favor, ingrese un número válido.");
            scanner.next();
        }
        int numero = scanner.nextInt();
        scanner.nextLine();
        return numero;
    }
}