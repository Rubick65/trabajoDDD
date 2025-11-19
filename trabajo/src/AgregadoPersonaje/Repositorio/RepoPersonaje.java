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

public class RepoPersonaje implements IRepositorioExtend<Personaje, Integer> {


    private final File archivo = new File("Personajes.json");
    private final ObjectMapper oM = new ObjectMapper();
    private final ObjectWriter writer = oM.writerWithDefaultPrettyPrinter();
    private static int contadorID;
    private Map<Integer, Personaje> listaPersonajes;

    /**
     * Se cargan los datos del json
     * @throws IOException si ocurre un error al cargar
     */
    public RepoPersonaje() throws IOException {
        recibirDatosFichero();
    }

    /**
     * Se buscan los personajes por su clase
     * @param clase a buscar
     * @return lista filtrada por clases
     */
    public List<Personaje> buscarPersonajesPorClases(Personaje.Clase clase) {
        return listaPersonajes.values().stream().filter(personaje -> personaje.getClase().equals(clase)).toList();
    }

    /**
     * Se obtiene mediante optional un personaje
     * @param id a buscar
     * @return personaje a buscar
     */
    @Override
    public Optional<Personaje> findByIdOptional(Integer id) {
        return Optional.ofNullable(listaPersonajes.get(id));
    }

    /**
     * Se obtiene una lista de todos los personajes
     * @return lista de personajes
     */
    @Override
    public List<Personaje> findAllToList() {
        return List.copyOf(listaPersonajes.values());
    }

    /**
     * Se cuenta la cantidad de personajes
     * @return cantidad
     * @throws IOException si no hay personajes
     */
    @Override
    public long count() throws IOException {
        return listaPersonajes.size();
    }

    /**
     * Se elimina un personaje mediante su id
     * @param id del personaje a eliminar
     * @throws IOException si no existe el id
     */
    @Override
    public void deleteById(Integer id) throws IOException {
        recibirDatosFichero();
        comprobarExistenciaClave(id);
        listaPersonajes.remove(id);
        guardarDatos();
    }

    /**
     * Se eliminan todos los personajes
     * @throws IOException si ocurre un error al guardar
     */
    @Override
    public void deleteAll() throws IOException {
        contadorID = 0;
        listaPersonajes = new HashMap<>();
        guardarDatos();
    }

    /**
     * Se comprueba la existencia de un personaje mediante su id
     * @param id a comprobar
     * @return si existe o no
     */
    @Override
    public boolean existsById(Integer id) {
        return listaPersonajes.containsKey(id);
    }

    /**
     * Se obtiene un personaje por su id
     * @param id a buscar
     * @return personaje con ese id
     * @throws IOException si no existe ese id
     */
    @Override
    public Personaje findById(Integer id) throws IOException {
        comprobarExistenciaClave(id);
        return listaPersonajes.get(id);
    }

    /**
     * Se obtiene la lista de personajes mediante iterable
     * @return lista de personajes
     */
    @Override
    public Iterable<Personaje> findAll() {
        return listaPersonajes.values();
    }

    /**
     * Se guardan los datos en el json
     * @param entity personaje a actualizar
     * @return devuelve el personaje actualizado
     * @param <S> entidad e hijos
     * @throws Exception en caso de problemas al actualizar
     */
    @Override
    public <S extends Personaje> S save(S entity) throws Exception {
        recibirDatosFichero();
        if (listaPersonajes.containsValue(entity))
            throw new IllegalArgumentException("El personaje " + entity.getNombrePersonaje() + " ya existe en el archivo");

        entity.setID_PERSONAJE(contadorID);
        listaPersonajes.put(entity.getID_PERSONAJE(), entity);
        guardarDatos();
        return entity;
    }

    /**
     * Actualiza la entidad si ya existía antes
     *
     * @param entity Entidad a actualizar
     * @param <S>    Entidad he hijos
     * @return Devuelve la entidad actualizada
     * @throws IOException Lanza excepción en caso de problemas a la hora de la escritura
     */
    public <S extends Personaje> S actualizarDatos(S entity) throws IOException {
        comprobarExistenciaClave(entity.getID_PERSONAJE());
        listaPersonajes.put(entity.getID_PERSONAJE(), entity);
        guardarDatos();
        return entity;
    }

    /**
     * Se guardan los datos en el json
     * @throws IOException en caso de error de escritura
     */
    private void guardarDatos() throws IOException {
        writer.writeValue(archivo, listaPersonajes);
    }

    /**
     * Se cargan los datos del json
     * @throws IOException en caso de error al cargar los datos
     */
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

    /**
     * Se comprueba la existencia del personaje mediante su id
     * @param id id a buscar
     * @throws IOException en caso de no existir
     */
    private void comprobarExistenciaClave(Integer id) throws IOException {
        if (!existsById(id))
            throw new IllegalArgumentException("En la lista no existe ningún personaje con este id");
    }
}

