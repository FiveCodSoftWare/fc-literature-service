package literature;

import literature.principal.AppPrincipal;
import literature.repository.AutorRepositorio;
import literature.repository.LibroRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FcLiteratureServiceApplication implements CommandLineRunner {

	private final AutorRepositorio repositorioA;
	private final LibroRepositorio repositorioL;


	public FcLiteratureServiceApplication(AutorRepositorio repositorioC, LibroRepositorio repositorioL) {
		this.repositorioA = repositorioC;
		this.repositorioL = repositorioL;
	}


	public static void main(String[] args) {
		SpringApplication.run(FcLiteratureServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		AppPrincipal appPrincipal = new AppPrincipal (repositorioA, repositorioL);

		appPrincipal.MostrarMenu();



	}
}
