package AgregadoAventura.Repositorio;

import AgregadoAventura.Aventura;
import AgregadoPersonaje.Personaje;
import Interfaces.IRepositorioExtend;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RepoAventura implements IRepositorioExtend {

    private final File archivo = new File("Aventuras.json");
    private final ObjectMapper oM = new ObjectMapper();
    private final ObjectWriter writer = oM.writerWithDefaultPrettyPrinter();
    private static int contadorID;
    private Map<Integer, Aventura> listaAventuras;


    public RepoAventura() throws IOException {
        recibirDatosFichero();
    }

    @Override
    public Optional<Aventura> findByIdOptional(Object o) {
        validacionId(o);
        return Optional.ofNullable(listaAventuras.get(o));
    }

    @Override
    public List<Aventura> findAllToList() {
        return List.copyOf(listaAventuras.values());
    }

    @Override
    public long count() throws IOException {
        return listaAventuras.size();
    }

    @Override
    public void deleteById(Object o) throws IOException {
        recibirDatosFichero();
        comprobarExistenciaClave(o);
        listaAventuras.remove(o);
        escribirDatos();
    }

    @Override
    public void deleteAll() throws IOException {
        contadorID = 0;
        listaAventuras = new HashMap<>();
        escribirDatos();
    }

    @Override
    public boolean existsById(Object o) {
        validacionId(o);
        return listaAventuras.containsKey(o);
    }

    @Override
    public Object findById(Object o){
        comprobarExistenciaClave(o);
        return listaAventuras.get(o);
    }

    @Override
    public Iterable<Aventura> findAll() {
        return listaAventuras.values();
    }

    @Override
    public Object save(Object entity) throws IOException {
        if (!(entity instanceof Aventura aventura))
            throw new IllegalArgumentException("El tipo de dato debe ser una aventura");

        recibirDatosFichero();
        if (listaAventuras.containsValue(aventura))
            throw new IllegalArgumentException("La aventura ya existe en el archivo");

        aventura.setID_AVENTURA(contadorID);
        listaAventuras.put(aventura.getID_AVENTURA(), aventura);
        escribirDatos();
        return aventura;
    }

    private void escribirDatos() throws IOException {
        writer.writeValue(archivo, listaAventuras);
    }

    private void recibirDatosFichero() throws IOException {
        if (archivo.exists() && archivo.length() > 0) {
            listaAventuras = oM.readValue(archivo, new TypeReference<Map<Integer, Aventura>>() {
            });
            /*
             Saca todos los ids del Hash Map y los compara en caso de que la lista este vacía se pondrá por defecto 1
             en caso contrario se pondrá último id + 1
             */
            contadorID = listaAventuras.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
        } else {
            listaAventuras = new HashMap<>();
        }
    }

    private void comprobarExistenciaClave(Object o) {
        if (!existsById(o))
            throw new IllegalArgumentException("En la lista no existe ninguna aventura con este id");
    }

    private static void validacionId(Object o) {
        if (!(o instanceof Integer)) throw new IllegalArgumentException("El ID debe ser numérico");
    }
}

