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

    private final File archivo = new File("gruposDeJuego.json");
    private final ObjectMapper oM = new ObjectMapper();
    private final ObjectWriter writer = oM.writerWithDefaultPrettyPrinter();
    private static int contadorID;
    private Map<Integer, GrupoJuego> listaGrupoDeJuego;


    private void comprobarExistenciaClave(Integer id) {
        if (!existsById(id))
            throw new IllegalArgumentException("En la lista no existe ningún grupo de juego con este id");
    }

    @Override
    public Optional<GrupoJuego> findByIdOptional(Integer id) {
        return Optional.ofNullable(listaGrupoDeJuego.get(id));
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
    public void deleteById(Integer id) throws IOException {
        recibirDatosFichero();
        comprobarExistenciaClave(id);
        listaGrupoDeJuego.remove(id);
        escribirDatos();

    }

    @Override
    public void deleteAll() throws IOException {
        listaGrupoDeJuego = new HashMap<>();
        escribirDatos();
    }

    @Override
    public boolean existsById(Integer id) {
        return listaGrupoDeJuego.containsKey(id);
    }

    @Override
    public GrupoJuego findById(Integer id) {
        comprobarExistenciaClave(id);
        return listaGrupoDeJuego.get(id);
    }

    @Override
    public Iterable<GrupoJuego> findAll() {
        return listaGrupoDeJuego.values();
    }

    @Override
    public <S extends GrupoJuego> S save(S entity) throws IOException {
        if (!(entity instanceof GrupoJuego grupoJuego))
            throw new IllegalArgumentException("El tipo de dato debe ser un Grupo de juego");
        recibirDatosFichero();
        if (listaGrupoDeJuego.containsValue(grupoJuego))
            throw new IllegalArgumentException("El grupo de juego ya existe en el archivo");

        grupoJuego.setID_GRUPO(contadorID);
        listaGrupoDeJuego.put(grupoJuego.getID_GRUPO(), grupoJuego);
        escribirDatos();
        return entity;
    }


    public RepoGrupoJuego() throws IOException {
        recibirDatosFichero();
    }


    private void escribirDatos() throws IOException {
        writer.writeValue(archivo, listaGrupoDeJuego);
    }

    private void recibirDatosFichero() throws IOException {
        if (archivo.exists() && archivo.length() > 0) {
            listaGrupoDeJuego = oM.readValue(archivo, new TypeReference<>() {
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

    public List<GrupoJuego> buscarGruposPorIdJugador(int idJugadorSeleccionado) {
        return listaGrupoDeJuego.values().stream()
                .filter(grupo -> grupo.getListaMiembros().contains(idJugadorSeleccionado))
                .toList();

    }
}
