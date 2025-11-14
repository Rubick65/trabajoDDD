package AgregadoJugador.Repositorio;

import AgregadoJugador.DireccionJuego;
import AgregadoJugador.Jugador;
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

public class RepoJugador implements IRepositorioExtend<Jugador, Integer> {

    private final File archivo = new File("Jugadores.json");
    private final ObjectMapper oM = new ObjectMapper();
    private final ObjectWriter writer = oM.writerWithDefaultPrettyPrinter();
    private static int contadorID;
    private Map<Integer, Jugador> listaJugadores;

    public RepoJugador() throws IOException {
        recibirDatosFichero();
    }

    private void comprobarExistenciaClave(Integer o) throws IOException {
        if (!existsById(o))
            throw new IllegalArgumentException("En la lista no existe ningún jugador con este id");
    }

    public List<Jugador> buscarJugadorPorDireccion(DireccionJuego direccionJuego) {
        return listaJugadores.values().stream().filter(jugador -> jugador.getDireccionJuego().equals(direccionJuego)).toList();
    }

    @Override
    public Optional<Jugador> findByIdOptional(Integer id) {
        return Optional.ofNullable(listaJugadores.get(id));
    }

    @Override
    public List<Jugador> findAllToList() {
        return List.copyOf(listaJugadores.values());
    }

    @Override
    public long count() {
        return listaJugadores.size();
    }

    @Override
    public void deleteById(Integer id) throws IOException {
        recibirDatosFichero();
        comprobarExistenciaClave(id);
        listaJugadores.remove(id);
        escribirDatos();
    }

    @Override
    public void deleteAll() throws IOException {
        contadorID = 0;
        listaJugadores = new HashMap<>();
        escribirDatos();
    }

    @Override
    public boolean existsById(Integer id) {
        return listaJugadores.containsKey(id);
    }

    @Override
    public Jugador findById(Integer id) throws IOException {
        comprobarExistenciaClave(id);
        return listaJugadores.get(id);
    }

    @Override
    public Iterable<Jugador> findAll() {
        return listaJugadores.values();
    }

    @Override
    public <S extends Jugador> S save(S entity) throws Exception {
        if (!(entity instanceof Jugador jugador))
            throw new IllegalArgumentException("El tipo de dato debe ser un Jugador");

        recibirDatosFichero();
        if (listaJugadores.containsValue(jugador))
            throw new IllegalArgumentException("El jugador ya existe en el archivo");

        jugador.setID_JUGADOR(contadorID);
        listaJugadores.put(jugador.getID_JUGADOR(), jugador);
        escribirDatos();
        return entity;
    }


    private void escribirDatos() throws IOException {
        Map<Integer, JsonNode> jsonMap = new HashMap<>();
        for (Map.Entry<Integer, Jugador> entry : listaJugadores.entrySet()) {
            // aquí usamos ObjectMapper directamente
            jsonMap.put(entry.getKey(), oM.valueToTree(entry.getValue()));
        }

        // Guardar JSON en archivo con pretty print
        oM.writerWithDefaultPrettyPrinter().writeValue(archivo, jsonMap);
    }


    private void recibirDatosFichero() throws IOException {
        if (archivo.exists() && archivo.length() > 0) {
            listaJugadores = oM.readValue(archivo, new TypeReference<Map<Integer, Jugador>>() {
            });
            /*
             Saca todos los ids del Hash Map y los compara en caso de que la lista este vacía se pondrá por defecto 1
             en caso contrario se pondrá último id + 1
             */
            contadorID = listaJugadores.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
        } else {
            listaJugadores = new HashMap<>();
        }
    }

}
