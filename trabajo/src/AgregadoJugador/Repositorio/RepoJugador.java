package AgregadoJugador.Repositorio;

import AgregadoJugador.Jugador;
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

public class RepoJugador implements IRepositorioExtend {

    private final File archivo = new File("Jugadores.json");
    private final ObjectMapper oM = new ObjectMapper();
    private final ObjectWriter writer = oM.writerWithDefaultPrettyPrinter();
    private static int contadorID;
    private Map<Integer, Jugador> listaJugadores;

    public RepoJugador() throws IOException {
        recibirDatosFichero();
    }

    @Override
    public Optional findByIdOptional(Object o) {
        validacionId(o);
        return Optional.ofNullable(listaJugadores.get(o));
    }

    @Override
    public List findAllToList() {
        return List.copyOf(listaJugadores.values());
    }

    @Override
    public long count() throws IOException {
        return listaJugadores.size();
    }

    @Override
    public void deleteById(Object o) throws IOException {
        recibirDatosFichero();
        comprobarExistenciaClave(o);
        listaJugadores.remove(o);
    }

    @Override
    public void deleteAll() throws IOException {
        contadorID = 0;
        listaJugadores = new HashMap<>();
        oM.writeValue(archivo, new HashMap<Integer, Jugador>());
    }

    @Override
    public boolean existsById(Object o) throws IOException {
        validacionId(o);
        return listaJugadores.containsKey(o);
    }

    @Override
    public Object findById(Object o) throws IOException {
        comprobarExistenciaClave(o);
        return listaJugadores.get(o);
    }

    @Override
    public Iterable findAll() {
        return listaJugadores.entrySet();
    }

    @Override
    public Object save(Object entity) throws IOException {
        if (!(entity instanceof Jugador jugador))
            throw new IllegalArgumentException("El tipo de dato debe ser un Jugador");

        if (listaJugadores.containsValue(jugador))
            throw new IllegalArgumentException("El jugador " + jugador.getNombre() + " con Dni " + jugador.getDNI() + " ya existe dentro de la lista");

        recibirDatosFichero();
        jugador.setID_JUGADOR(contadorID);
        listaJugadores.put(jugador.getID_JUGADOR(), jugador);
        writer.writeValue(archivo, listaJugadores);
        return jugador;

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

    private void comprobarExistenciaClave(Object o) throws IOException {
        if (!existsById(o))
            throw new IllegalArgumentException("En la lista no existe ningún jugador con este id");
    }

    private static void validacionId(Object o) {
        if (!(o instanceof Integer)) throw new IllegalArgumentException("El ID debe ser numérico");
    }

}
