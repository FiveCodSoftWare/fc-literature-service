package literature.utils;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import literature.model.libro.DatosLibro;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UtilsDatos (@JsonAlias("results") List<DatosLibro> libroList) {}