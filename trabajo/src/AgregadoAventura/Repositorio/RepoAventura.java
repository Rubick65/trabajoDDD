package AgregadoAventura.Repositorio;

import AgregadoAventura.Aventura;
import Interfaces.IRepositorioExtend;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RepoAventura implements IRepositorioExtend<Aventura, Integer> {

    private final File archivo = new File("trabajo/src/AgregadoAventura/Aventura.json");
    private final ObjectMapper oM = new ObjectMapper();
    private static int contadorID;
    private Map<Integer, Aventura> listaAventuras;

    /**
     * Se cargan los datos al crear el repositorio
     * @throws IOException si hay un error al cargar
     */
    public RepoAventura() throws IOException {
        recibirDatosFichero();
    }

    /**
     * Se buscan las aventuras por su dificultad
     * @param dificultad dificultad a buscar
     * @return lista filtrada con todas esas dificultades
     */
    public List<Aventura> buscarAventuraPorDificultad(Aventura.Dificultad dificultad) {
        return listaAventuras.values().stream().filter(aventura -> aventura.getDificultad().equals(dificultad)).toList();
    }

    /**
     * Se busca una aventura por id usando optional
     * @param id a buscar
     * @return la aventura con ese id
     */
    @Override
    public Optional<Aventura> findByIdOptional(Integer id) {
        return Optional.ofNullable(listaAventuras.get(id));
    }

    /**
     * Se muestran todas las aventuras
     * @return todas las aventuras
     */
    @Override
    public List<Aventura> findAllToList() {
        return List.copyOf(listaAventuras.values());
    }

    /**
     * Se cuentan todas las aventuras
     * @return cantidad
     * @throws IOException si esta vacio
     */
    @Override
    public long count() throws IOException {
        return listaAventuras.size();
    }

    /**
     * Se elimina una aventura mediante su id
     * @param id de la aventura a eliminar
     * @throws IOException si no existe.
     */
    @Override
    public void deleteById(Integer id) throws IOException {
        recibirDatosFichero();
        comprobarExistenciaClave(id);
        listaAventuras.remove(id);
        guardarDatos();
    }

    /**
     * Se borran todas las aventuras
     * @throws IOException
     */
    @Override
    public void deleteAll() throws IOException {
        contadorID = 0;
        listaAventuras = new HashMap<>();
        guardarDatos();
    }

    /**
     * Se comprueba si existe una aventura con el id dado
     * @param id a buscar
     * @return si existe o no
     */
    @Override
    public boolean existsById(Integer id) {
        return listaAventuras.containsKey(id);
    }

    /**
     * Se busca una aventura mediante su id
     * @param id a buscar
     * @return aventura con ese id
     * @throws IOException si no existe una aventura con ese id
     */
    @Override
    public Aventura findById(Integer id) throws IOException {
        comprobarExistenciaClave(id);
        return listaAventuras.get(id);
    }

    /**
     * Se obtienen todas las aventuras con iterable
     * @return todas las aventuras
     */
    @Override
    public Iterable<Aventura> findAll() {
        return listaAventuras.values();
    }

    /**
     * Se guardan las aventuras en la lista y se guardan
     * @param entity aventura a guardar
     * @return aventura a guardar
     * @param <S> entidad e hijos
     * @throws Exception en caso de problema al guardar
     */
    @Override
    public <S extends Aventura> S save(S entity) throws Exception {
        recibirDatosFichero();
        if (listaAventuras.containsValue(entity))
            throw new IllegalArgumentException("La aventura " + entity.getNombreAventura() + " ya existe en el archivo");

        entity.setID_AVENTURA(contadorID);
        listaAventuras.put(entity.getID_AVENTURA(), entity);
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
    public <S extends Aventura> S actualizarDatos(S entity) throws IOException {
        comprobarExistenciaClave(entity.getID_AVENTURA());
        listaAventuras.put(entity.getID_AVENTURA(), entity);
        guardarDatos();
        return entity;
    }

    /**
     * Se guardan los datos en el json
     * @throws IOException si ocurre un error al guardar
     */
    private void guardarDatos() throws IOException {
        Map<Integer, JsonNode> jsonMap = new HashMap<>();
        for (Map.Entry<Integer, Aventura> entry : listaAventuras.entrySet()) {
            jsonMap.put(entry.getKey(), oM.valueToTree(entry.getValue()));
        }
        oM.writerWithDefaultPrettyPrinter().writeValue(archivo, jsonMap);
    }

    /**
     * Se cargan los datos del json
     * @throws IOException si ocurre un error al cargar
     */
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

    /**
     * Se comprueba si existe una aventura por su id
     * @param id id a buscar
     */
    private void comprobarExistenciaClave(Integer id) {
        if (!existsById(id))
            throw new IllegalArgumentException("En la lista no existe ninguna aventura con este id");
    }
}

