package AgregadoAventura.Repositorio;

import AgregadoAventura.Aventura;
import GestorBaseDeDatos.GestorDB;
import Interfaces.IRepositorioExtend;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.function.Function;

public class RepoAventura implements IRepositorioExtend<Aventura, Integer> {

    private final File archivo = new File("trabajo/src/AgregadoAventura/Aventura.json");
    private final ObjectMapper oM = new ObjectMapper();
    private static int contadorID;
    private Map<Integer, Aventura> listaAventuras;
    private GestorDB gestorDB;

    /**
     * Se cargan los datos al crear el repositorio
     * @throws IOException si hay un error al cargar
     */
    public RepoAventura() throws IOException {
        this.gestorDB = new GestorDB("Aventura");
    }

    /**
     * Se buscan las aventuras por su dificultad
     * @param dificultad dificultad a buscar
     * @return lista filtrada con todas esas dificultades
     */
    public List<Aventura> buscarAventuraPorDificultad(Aventura.Dificultad dificultad, Function <ResultSet,Aventura> parsearAventura) throws IOException, SQLException {
       List<Aventura> listaAventuras = new ArrayList<>();
       try (Connection conexion = gestorDB.crearConexion()) {

           // Select ha realizar
           String select = "select * from " + gestorDB.getTabla() + " where dificultad = ?";
           PreparedStatement ps = conexion.prepareStatement(select);
           ps.setString(1, dificultad.toString());

           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               //Por cada linea, se parsea al objeto indicado
               listaAventuras.add(parsearAventura.apply(rs));
           }
       }
       return listaAventuras;
    }

    /**
     * Se busca una aventura por id usando optional
     * @param id a buscar
     * @return la aventura con ese id
     */
    @Override
    public Optional<Aventura> findByIdOptional(Integer id) throws IOException {
        return Optional.empty();

    }

    /**
     * Se muestran todas las aventuras
     * @return todas las aventuras
     */
    @Override
    public List<Aventura> findAllToList() throws IOException {
        return null;

    }

    /**
     * Se cuentan todas las aventuras
     * @return cantidad
     * @throws IOException si esta vacio
     */
    @Override
    public long count() throws IOException {
        return 0;

    }

    /**
     * Se elimina una aventura mediante su id
     * @param id de la aventura a eliminar
     * @throws IOException si no existe.
     */
    @Override
    public void deleteById(Integer id) throws IOException {

    }

    /**
     * Se borran todas las aventuras
     * @throws IOException
     */
    @Override
    public void deleteAll() throws IOException {

    }

    /**
     * Se comprueba si existe una aventura con el id dado
     * @param id a buscar
     * @return si existe o no
     */
    @Override
    public boolean existsById(Integer id) throws IOException {
        return false;

    }

    /**
     * Se busca una aventura mediante su id
     * @param id a buscar
     * @return aventura con ese id
     * @throws IOException si no existe una aventura con ese id
     */
    @Override
    public Aventura findById(Integer id) throws IOException {
        return null;

    }

    /**
     * Se obtienen todas las aventuras con iterable
     * @return todas las aventuras
     */
    @Override
    public Iterable<Aventura> findAll() throws IOException {
        return null;

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
        return null;

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
        return null;

    }

    /**
     * Se guardan los datos en el json
     * @throws IOException si ocurre un error al guardar
     */
    private void guardarDatos() throws IOException {

    }

    /**
     * Se comprueba si existe una aventura por su id
     * @param id id a buscar
     */
    private void comprobarExistenciaClave(Integer id) throws IOException {
        if (!existsById(id))
            throw new IllegalArgumentException("En la lista no existe ninguna aventura con este id");
    }
}

