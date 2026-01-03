package AgregadoJugador.Repositorio;

import AgregadoJugador.Jugador;
import Interfaces.IRepositorioExtend;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RepoJugador implements IRepositorioExtend<Jugador, Integer> {

    // XX Borrar todos los atributos de clase
    private final File archivo = new File("trabajo/src/AgregadoJugador/Jugadores.json");// Fichero donde se guardan los jugadores
    private final ObjectMapper oM = new ObjectMapper(); // Objeto que permite parsear los datos a json y de json a java
    private static int contadorID;// Contador auto incremental para los jugadores
    private Map<Integer, Jugador> listaJugadores; // Clave id de jugadores y el valor el jugador

    /**
     * Constructor que saca los datos del fichero
     *
     * @throws IOException Lanza una excepción en caso de no haber encontrado el fichero
     */
    public RepoJugador() throws IOException {
        recibirDatosFichero();
    }

    /**
     * Busca en la lista de jugadores por el id introducido
     *
     * @param id Id introducido por el usuario
     * @return En caso de existir el jugador devuelve ese jugador, en caso contrario Optional.empty()
     */
    @Override
    public Optional<Jugador> findByIdOptional(Integer id) throws IOException {
        recibirDatosFichero();
        return Optional.ofNullable(listaJugadores.get(id));
    }

    /**
     * Devuelve todos los jugadores en una lista
     *
     * @return Devuelve los jugadores en un objeto tipo List
     */
    @Override
    public List<Jugador> findAllToList() throws IOException {
        recibirDatosFichero();
        return List.copyOf(listaJugadores.values());
    }

    /**
     * Cuenta la cantidad de jugadores presentes en la lista
     *
     * @return Devuelve la cantidad de jugadores en la lista
     */
    @Override
    public long count() throws IOException {
        recibirDatosFichero();
        return listaJugadores.size();
    }

    /**
     * Eliminina un jugdor
     *
     * @param id Id del jugador a eliminar
     * @throws IOException lanza una excepción si falla al leer el archivo
     */
    @Override
    public void deleteById(Integer id) throws IOException {
        //lee el archivo
        recibirDatosFichero();
        // Se asegura de que el id si exista en el archivo
        comprobarExistenciaClave(id);
        // En caso de si existir lo elimina
        listaJugadores.remove(id);
        // Y escribe esos datos en el fichero
        guardarDatos();
    }

    /**
     * Elimina todos los datos del fichero
     *
     * @throws IOException Lanza la excepción cuando no se puede leer correctamente el archivoi
     */
    @Override
    public void deleteAll() throws IOException {
        // Primero vacía la lista de jugadores
        listaJugadores = new HashMap<>();
        // Luego escribe esa lista vacía en el fichero
        guardarDatos();
    }

    /**
     * Comprueba si existe un jugador con el id pasado como parámetro
     *
     * @param id Id del jugador a buscar
     * @return Devuelve true en caso de que si exista y false en caso contrario
     */
    @Override
    public boolean existsById(Integer id) throws IOException {
        recibirDatosFichero();
        return listaJugadores.containsKey(id);
    }

    /**
     * Busca un jugador por su id
     *
     * @param id Id del jugador a buscar
     * @return Devuelve en caso de encontrarlo el jugador buscado
     */
    @Override
    public Jugador findById(Integer id) throws IOException {
        recibirDatosFichero();
        comprobarExistenciaClave(id);
        return listaJugadores.get(id);
    }

    /**
     * Devuelve una colección con todos los jugadores
     *
     * @return Devuelve una colección con todos los jugadores
     */
    @Override
    public Iterable<Jugador> findAll() throws IOException {
        recibirDatosFichero();
        return listaJugadores.values();
    }

    /**
     * Guarda el jugador en el fichero
     *
     * @param entity Entidad que se va a guardar
     * @param <S>    Objeto que extiende de jugador
     * @return Devuele la entidad guardada
     * @throws IllegalArgumentException Lanza esta excepción en caso de que la lista ya contenga a ese jugador
     * @throws IOException              Lanza este error cuando la lectura del archivo falla
     */
    @Override
    public <S extends Jugador> S save(S entity) throws IllegalArgumentException, IOException {
        // Actualizamos los datos del fichero
        recibirDatosFichero();
        // Si la lista de jugadores contiene al jugador pasado como parámetro
        if (listaJugadores.containsValue(entity))
            // Se lanza una excepción que indica que este jugador ya existe
            throw new IllegalArgumentException("El jugador " + entity.getNombre() + " ya existe en el archivo");

        // En caso de que no exista se le pone un id
        entity.setID_JUGADOR(contadorID);
        // Se añade el jugador a la lista
        listaJugadores.put(entity.getID_JUGADOR(), entity);
        // Y se sobreescribe la lista en el fichero
        guardarDatos();
        // Devolvemos la entidad guardada
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
    public <S extends Jugador> S actualizarDatos(S entity) throws IOException {
        comprobarExistenciaClave(entity.getID_JUGADOR());
        listaJugadores.put(entity.getID_JUGADOR(), entity);
        guardarDatos();
        return entity;
    }


    /**
     * Escribe los datos en el archivo
     *
     * @throws IOException Lanza excepción en caso de que la escritura falle
     */
    private void guardarDatos() throws IOException {
        // Creamos un map con Json node
        Map<Integer, JsonNode> jsonMap = new HashMap<>();
        // Recorremos toda la lista de jugadores
        for (Map.Entry<Integer, Jugador> entry : listaJugadores.entrySet()) {
            // Y vamos añadiendo cada posición al Json Map, esto fuerza a que se apliquen las anotaciones
            jsonMap.put(entry.getKey(), oM.valueToTree(entry.getValue()));
        }

        // Guardar JSON en archivo con pretty print,esto hace que el archivo sea más bonito y legible
        oM.writerWithDefaultPrettyPrinter().writeValue(archivo, jsonMap);
    }


    /**
     * Recibe los datos del archivo
     * XX BORRAR Método
     *
     * @throws IOException Lanza excepción en caso de que la lectura del archivo falle
     */
    private void recibirDatosFichero() throws IOException {
        // Si el archivo existe y su longitud es mayor que cero
        if (archivo.exists() && archivo.length() > 0) {
            // Sacamos del archivo la lista de jugadores
            listaJugadores = oM.readValue(archivo, new TypeReference<Map<Integer, Jugador>>() {
            });

        } else {
            // En caso contrario inicializamos la lista vacía
            listaJugadores = new HashMap<>();
        }
         /*
             Saca todos los ids del Hash Map y los compara, sacando el mayor
             en caso de que la lista este vacía se pondrá por defecto 1,
             en caso contrario se pondrá último id + 1
             */
        contadorID = listaJugadores.keySet().stream().max(Integer::compareTo).orElse(0) + 1;

    }

    /**
     * Se asegura de que la clave pasada exista dentro del fichero
     *
     * @param id a comprobar
     */
    private void comprobarExistenciaClave(Integer id) throws IllegalArgumentException, IOException {
        if (!existsById(id))
            throw new IllegalArgumentException("En la lista no existe ningún jugador con id " + id);
    }

    /**
     * Método añadido que se encarga de buscar jugadores por su calle
     *
     * @param calle Calle introducida por el usuario para buscar todos los jugadores que tengan esa calle como dirección de juego
     * @return Devuelve una lista con todos los jugadores que viven en la calle seleccionada
     */
    public List<Jugador> buscarJugadorPorDireccion(String calle) throws IOException {
        recibirDatosFichero();
        // Filtramos la lista de jugadores por aquellos que vivan en la calle pasada como parámetro
        return listaJugadores.values().stream().filter(jugador -> jugador.getDireccionJuego().getCalle().equalsIgnoreCase(calle)).toList();
    }

}
