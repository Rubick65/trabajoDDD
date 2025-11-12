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

public class RepoGrupoJuego implements IRepositorioExtend {

    private final File archivo = new File("gruposDeJuego.json");
    private final ObjectMapper oM = new ObjectMapper();
    private final ObjectWriter writer = oM.writerWithDefaultPrettyPrinter();
    private static int contadorID;
    private Map<Integer, GrupoJuego> listaGrupoDeJuego;

    public RepoGrupoJuego() throws IOException {
        recibirDatosFichero();
    }

    @Override
    public Optional<GrupoJuego> findByIdOptional(Object o) {
        return Optional.ofNullable(listaGrupoDeJuego.get(o));
    }

    @Override
    public List<GrupoJuego> findAllToList() {
        return List.copyOf(listaGrupoDeJuego.values());
    }

    @Override
    public long count() {
        return listaGrupoDeJuego.size();
    }

    @Override
    public void deleteById(Object o) throws IOException {
        recibirDatosFichero();
        comprobarExistenciaClave(o);
        listaGrupoDeJuego.remove(o);
        escribirDatos();
    }

    @Override
    public void deleteAll() throws IOException {
        contadorID = 0;

        listaGrupoDeJuego = new HashMap<>();
        escribirDatos();
    }

    @Override
    public boolean existsById(Object o) {
        validacionId(o);
        return listaGrupoDeJuego.containsKey(o);
    }

    @Override
    public Object findById(Object o) {
        comprobarExistenciaClave(o);
        return listaGrupoDeJuego.get(o);
    }

    @Override
    public Iterable<GrupoJuego> findAll() {
        return listaGrupoDeJuego.values();
    }

    @Override
    public Object save(Object entity) throws IOException {
        if (!(entity instanceof GrupoJuego grupoJuego))
            throw new IllegalArgumentException("El tipo de dato debe ser un Grupo de juego");
        recibirDatosFichero();
        if (!listaGrupoDeJuego.containsKey(grupoJuego.getID_GRUPO()))
            grupoJuego.setID_GRUPO(contadorID);


        listaGrupoDeJuego.put(grupoJuego.getID_GRUPO(), grupoJuego);
        escribirDatos();
        return grupoJuego;
    }

    private void escribirDatos() throws IOException {
        writer.writeValue(archivo, listaGrupoDeJuego);
    }

    private void recibirDatosFichero() throws IOException {
        if (archivo.exists() && archivo.length() > 0) {
            listaGrupoDeJuego = oM.readValue(archivo, new TypeReference<Map<Integer, GrupoJuego>>() {
            });
            /*
             Saca todos los ids del Hash Map y los compara en caso de que la lista este vacía se pondrá por defecto 1
             en caso contrario se pondrá último id + 1
             */
            contadorID = listaGrupoDeJuego.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
        } else {
            listaGrupoDeJuego = new HashMap<>();
        }
    }

    public List<GrupoJuego> buascarGruposPorIdJugador(int idJugador) {
        return listaGrupoDeJuego.values().stream()
                .filter(grupo -> grupo.getListaMiembros().contains(idJugador))
                .toList();

    }

    private void comprobarExistenciaClave(Object o) {
        if (!existsById(o))
            throw new IllegalArgumentException("En la lista no existe ningún grupo de juego con este id");
    }

    private static void validacionId(Object o) {
        if (!(o instanceof Integer)) throw new IllegalArgumentException("El ID debe ser numérico");
    }
}
