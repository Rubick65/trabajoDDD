package AgregadoJugador.Repositorio;

import AgregadoGrupoJuego.GrupoJuego;
import AgregadoGrupoJuego.Repositorio.RepoGrupoJuego;
import AgregadoJugador.DireccionJuego;
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
    RepoGrupoJuego repoGrupoJuego = new RepoGrupoJuego();


    public RepoJugador() throws IOException {
        recibirDatosFichero();
    }

    @Override
    public Optional<Jugador> findByIdOptional(Object o) {
        validacionId(o);
        return Optional.ofNullable(listaJugadores.get(o));
    }

    @Override
    public List<Jugador> findAllToList() {
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
        eliminarIdJugadorGrupoJuego(o);
        listaJugadores.remove(o);
        escribirDatos();
    }

    @Override
    public void deleteAll() throws IOException {
        contadorID = 0;
        repoGrupoJuego.deleteAll();
        listaJugadores = new HashMap<>();
        escribirDatos();
    }

    @Override
    public boolean existsById(Object o) {
        validacionId(o);
        return listaJugadores.containsKey(o);
    }

    @Override
    public Object findById(Object o) throws IOException {
        comprobarExistenciaClave(o);
        return listaJugadores.get(o);
    }

    @Override
    public Iterable<Jugador> findAll() {
        return listaJugadores.values();
    }

    @Override
    public Object save(Object entity) throws IOException {
        if (!(entity instanceof Jugador jugador))
            throw new IllegalArgumentException("El tipo de dato debe ser un Jugador");

        recibirDatosFichero();
        if (listaJugadores.containsValue(jugador))
            throw new IllegalArgumentException("El jugador ya existe en el archivo");

        jugador.setID_JUGADOR(contadorID);
        listaJugadores.put(jugador.getID_JUGADOR(), jugador);
        escribirDatos();
        return jugador;
    }

    private void escribirDatos() throws IOException {
        writer.writeValue(archivo, listaJugadores);
    }

    private void eliminarIdJugadorGrupoJuego(Object idJugador) throws IOException {
        List<GrupoJuego> listaGrupos = repoGrupoJuego.findAllToList();
        listaGrupos.forEach(grupoJuego -> {
            grupoJuego.eliminarJugador((int) idJugador);
        });
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

    public List<Jugador> buscarJugadorPorDireccion(DireccionJuego direccionJuego) {
        return listaJugadores.values().stream().filter(jugador -> jugador.getDireccionJuego().equals(direccionJuego)).toList();
    }

    private static void validacionId(Object o) {
        if (!(o instanceof Integer)) throw new IllegalArgumentException("El ID debe ser numérico");
    }

}
