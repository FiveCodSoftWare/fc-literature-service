package literature.service;

public interface ICambiarDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
