package AgregadoPersonaje.Repositorio;

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

public class RepoPersonaje implements IRepositorioExtend {


    private final File archivo = new File("Personajes.json");
    private final ObjectMapper oM = new ObjectMapper();
    private final ObjectWriter writer = oM.writerWithDefaultPrettyPrinter();
    private static int contadorID;
    private Map<Integer, Personaje> listaPersonajes;


    public RepoPersonaje() throws IOException {
        recibirDatosFichero();
    }

    @Override
    public Optional<Personaje> findByIdOptional(Object o) {
        validacionId(o);
        return Optional.ofNullable(listaPersonajes.get(o));
    }

    @Override
    public List<Personaje> findAllToList() {
        return List.copyOf(listaPersonajes.values());
    }

    @Override
    public long count() throws IOException {
        return listaPersonajes.size();
    }

    @Override
    public void deleteById(Object o) throws IOException {
        recibirDatosFichero();
        comprobarExistenciaClave(o);
        listaPersonajes.remove(o);
        escribirDatos();
    }

    @Override
    public void deleteAll() throws IOException {
        contadorID = 0;
        listaPersonajes = new HashMap<>();
        escribirDatos();
    }

    @Override
    public boolean existsById(Object o) {
        validacionId(o);
        return listaPersonajes.containsKey(o);
    }

    @Override
    public Object findById(Object o){
        comprobarExistenciaClave(o);
        return listaPersonajes.get(o);
    }

    @Override
    public Iterable<Personaje> findAll() {
        return listaPersonajes.values();
    }

    @Override
    public Object save(Object entity) throws IOException {
        if (!(entity instanceof Personaje personaje))
            throw new IllegalArgumentException("El tipo de dato debe ser un personaje");

        recibirDatosFichero();
        if (listaPersonajes.containsValue(personaje))
            throw new IllegalArgumentException("El personaje ya existe en el archivo");

        personaje.setID_PERSONAJE(contadorID);
        listaPersonajes.put(personaje.getID_PERSONAJE(), personaje);
        escribirDatos();
        return personaje;
    }

    private void escribirDatos() throws IOException {
        writer.writeValue(archivo, listaPersonajes);
    }

    private void recibirDatosFichero() throws IOException {
        if (archivo.exists() && archivo.length() > 0) {
            listaPersonajes = oM.readValue(archivo, new TypeReference<Map<Integer, Personaje>>() {
            });
            /*
             Saca todos los ids del Hash Map y los compara en caso de que la lista este vacía se pondrá por defecto 1
             en caso contrario se pondrá último id + 1
             */
            contadorID = listaPersonajes.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
        } else {
            listaPersonajes = new HashMap<>();
        }
    }

    private void comprobarExistenciaClave(Object o) {
        if (!existsById(o))
            throw new IllegalArgumentException("En la lista no existe ningún personaje con este id");
    }

    private static void validacionId(Object o) {
        if (!(o instanceof Integer)) throw new IllegalArgumentException("El ID debe ser numérico");
    }
}

