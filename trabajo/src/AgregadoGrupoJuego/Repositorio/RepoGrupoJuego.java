package AgregadoGrupoJuego.Repositorio;

import AgregadoGrupoJuego.GrupoJuego;
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

public class RepoGrupoJuego implements IRepositorioExtend<GrupoJuego, Integer> {

    private final File archivo = new File("gruposDeJuego.json");// Archivo donde se guardan los grupos
    private final ObjectMapper oM = new ObjectMapper(); // Mapper para leer y guardar los datos
    private final ObjectWriter writer = oM.writerWithDefaultPrettyPrinter(); // Para escribir y que quede bonito
    private static int contadorID; // Contador para el id de cada grupo
    private Map<Integer, GrupoJuego> listaGrupoDeJuego; // Lista con los grupos de los jugadors


    /**
     * Comprueba si existe un grupo con el id pasado como parámetro
     *
     * @param id id a comprobar
     */
    private void comprobarExistenciaClave(Integer id) {
        // Si el id no existe
        if (!existsById(id))
            // Lanza una excepción
            throw new IllegalArgumentException("En la lista no existe ningún grupo de juego con este id");
    }

    /**
     * Busca un jugador por id
     *
     * @param id Id del jugador a buscar
     * @return Devuelve el jugador si existe y un optional.empty() en caso contrario
     */
    @Override
    public Optional<GrupoJuego> findByIdOptional(Integer id) {
        return Optional.ofNullable(listaGrupoDeJuego.get(id));
    }

    /**
     * Devuelve todos los jugadores a una lista
     *
     * @return Una lista con todos los jugadores
     */
    @Override
    public List<GrupoJuego> findAllToList() {
        return List.copyOf(listaGrupoDeJuego.values());
    }

    /**
     * Cantidad de jugadores totales
     *
     * @return Devuelve la cantidad de jugadores totales
     */
    @Override
    public long count() {
        return listaGrupoDeJuego.size();
    }

    /**
     * Elimina un grupo por su id
     *
     * @param id Id del grupo a eliminar
     * @throws IOException Lanza excepción en caso de problema en la lectura o escritura del fichero
     */
    @Override
    public void deleteById(Integer id) throws IOException {
        // Actulizamos los datos
        recibirDatosFichero();
        // Comprobamos si existe algún grupo con este id
        comprobarExistenciaClave(id);
        // Si exsite lo eliminamos
        listaGrupoDeJuego.remove(id);
        // Actulizamos los datos
        guardarDatos();
    }

    /**
     * Eliminamos todos los datos del fichero y la lista
     *
     * @throws IOException Lanza excepción si encuentra problemas a la hora de actualizar los datos
     */
    @Override
    public void deleteAll() throws IOException {
        // Vaciamos la lista
        listaGrupoDeJuego = new HashMap<>();
        // Actualizamos los datos del fichero
        guardarDatos();
    }

    /**
     * Comprueba si existe un grupo con el id pasado
     *
     * @param id Id del grupo a buscar
     * @return Si existe devuelve true y si no devuelve false
     */
    @Override
    public boolean existsById(Integer id) {
        return listaGrupoDeJuego.containsKey(id);
    }

    /**
     * Busca un grupo por su id
     *
     * @param id Id del grupo a buscar
     * @return Devuelve el jugador buscado si lo encuentra
     */
    @Override
    public GrupoJuego findById(Integer id) {
        comprobarExistenciaClave(id);
        return listaGrupoDeJuego.get(id);
    }

    /**
     * Devuelve una lista de valores iterables
     *
     * @return Devuelve una lista de valores iterables
     */
    @Override
    public Iterable<GrupoJuego> findAll() {
        return listaGrupoDeJuego.values();
    }

    /**
     * Guarda los datos en el fichero
     *
     * @param entity Entidad a guardar
     * @param <S>    Grupo a guardar
     * @return Devuelve la entidad que se ha guardado
     * @throws IOException Lanza excepción en caso de fallar a la hora de leer o escribir los datos
     */
    @Override
    public <S extends GrupoJuego> S save(S entity) throws IOException {

        // Actualizamos los datos
        recibirDatosFichero();
        // Si la lista ya contiene un grupo igual
        if (listaGrupoDeJuego.containsValue(entity))
            // Lanza una excepción
            throw new IllegalArgumentException("El grupo de juego ya existe en el archivo");

        // Si el grupo no existe le ponemos un id
        entity.setID_GRUPO(contadorID);
        // Introducimos el grupo a la lista
        listaGrupoDeJuego.put(entity.getID_GRUPO(), entity);
        // Actualizamos los datos del grupo
        guardarDatos();
        // Devolvemos la entidad guardada
        return entity;
    }


    /**
     * Constructor que inicializa la lista de grupos
     *
     * @throws IOException Lanza una excepción si ocurre un error a la hora de leer la lista
     */
    public RepoGrupoJuego() throws IOException {
        recibirDatosFichero();
    }


    /**
     * Actualiza la entidad si ya existía antes
     *
     * @param entity Entidad a actualizar
     * @param <S>    Entidad he hijos
     * @return Devuelve la entidad actualizada
     * @throws IOException Lanza excepción en caso de problemas a la hora de la escritura
     */
    public <S extends GrupoJuego> S actualizarDatos(S entity) throws IOException {
        comprobarExistenciaClave(entity.getID_GRUPO());
        listaGrupoDeJuego.put(entity.getID_GRUPO(), entity);
        guardarDatos();
        return entity;
    }

    /**
     * Actualiza los datos del fichero
     *
     * @throws IOException Lanza una excepción si a la hora de actualizar ocurre algún problema con el fichero
     */
    private void guardarDatos() throws IOException {
        writer.writeValue(archivo, listaGrupoDeJuego);
    }

    /**
     * Recibe los datos del fichero
     *
     * @throws IOException Lanza excepción si ocurre algún problema a la hora de sacar los datos del fichero
     */
    private void recibirDatosFichero() throws IOException {
        // Si el archivo existe y es mayor que cero
        if (archivo.exists() && archivo.length() > 0) {
            // Cargamos los datos
            listaGrupoDeJuego = oM.readValue(archivo, new TypeReference<>() {
            });
            /*
             Saca todos los ids del Hash Map y los compara en caso de que la lista este vacía se pondrá por defecto 1
             en caso contrario se pondrá último id + 1
             */
            contadorID = listaGrupoDeJuego.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
        } else {
            // Si no simplemente inicializamos vacío
            listaGrupoDeJuego = new HashMap<>();
        }
    }

    /**
     * Busca todos los grupos de juego que contengan al jugador pasado como parámetro
     *
     * @param idJugadorSeleccionado Id del jugador a buscar en los grupos
     * @return Devuelve los grupos de los jugadores pasados como parámetros
     */
    public List<GrupoJuego> buscarGruposPorIdJugador(int idJugadorSeleccionado) {
        return listaGrupoDeJuego.values().stream()
                .filter(grupo -> grupo.getListaMiembros().contains(idJugadorSeleccionado))
                .toList();

    }
}
