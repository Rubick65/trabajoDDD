package AgregadoGrupoJuego.Repositorio;

import AgregadoGrupoJuego.GrupoJuego;
import GestorBaseDeDatos.GestorDB;
import GestorBaseDeDatos.GestorDeParseadores;
import Interfaces.IRepositorioExtend;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RepoGrupoJuego implements IRepositorioExtend<GrupoJuego, Integer> {


    private final GestorDB gestorGrupoDeJuego;
    private final String tabla = "GrupoJuego";
    private final String nombreId = "ID_GRUPO";


    /**
     * Constructor que inicializa la lista de grupos
     *
     * @throws IOException Lanza una excepción si ocurre un error a la hora de leer la lista
     */
    public RepoGrupoJuego() throws IOException {
        gestorGrupoDeJuego = new GestorDB(tabla);
    }


    /**
     * Busca un jugador por id
     *
     * @param id Id del jugador a buscar
     * @return Devuelve el jugador si existe y un optional.empty() en caso contrario
     */
    @Override
    public Optional<GrupoJuego> findByIdOptional(Integer id) throws IOException {
        return Optional.ofNullable(gestorGrupoDeJuego.findById(id, nombreId, GestorDeParseadores.parseadorGrupoJuego(gestorGrupoDeJuego.crearConexion())));
    }

    /**
     * Devuelve todos los jugadores a una lista
     *
     * @return Una lista con todos los jugadores
     */
    @Override
    public List<GrupoJuego> findAllToList() throws IOException {
        return gestorGrupoDeJuego.findAllToList(GestorDeParseadores.parseadorGrupoJuego(gestorGrupoDeJuego.crearConexion()));

    }

    /**
     * Cantidad de jugadores totales
     *
     * @return Devuelve la cantidad de jugadores totales
     */
    @Override
    public long count() throws IOException {
        return gestorGrupoDeJuego.count();

    }

    /**
     * Elimina un grupo por su id
     *
     * @param id Id del grupo a eliminar
     * @throws IOException Lanza excepción en caso de problema en la lectura o escritura del fichero
     */
    @Override
    public void deleteById(Integer id) throws IOException {
        gestorGrupoDeJuego.deleteById(id, nombreId);
    }

    /**
     * Eliminamos todos los datos del fichero y la lista
     *
     * @throws IOException Lanza excepción si encuentra problemas a la hora de actualizar los datos
     */
    @Override
    public void deleteAll() throws IOException {
        gestorGrupoDeJuego.deleteAll();

    }

    /**
     * Comprueba si existe un grupo con el id pasado
     *
     * @param id Id del grupo a buscar
     * @return Si existe devuelve true y si no devuelve false
     */
    @Override
    public boolean existsById(Integer id) throws IOException {
        return gestorGrupoDeJuego.existById(id, nombreId);
    }

    /**
     * Busca un grupo por su id
     *
     * @param id Id del grupo a buscar
     * @return Devuelve el jugador buscado si lo encuentra
     */
    @Override
    public GrupoJuego findById(Integer id) throws IOException {
        return gestorGrupoDeJuego.findById(id, nombreId, GestorDeParseadores.parseadorGrupoJuego(gestorGrupoDeJuego.crearConexion()));
    }

    /**
     * Devuelve una lista de valores iterables
     *
     * @return Devuelve una lista de valores iterables
     */
    @Override
    public Iterable<GrupoJuego> findAll() throws IOException {
        return gestorGrupoDeJuego.findAllToList(GestorDeParseadores.parseadorGrupoJuego(gestorGrupoDeJuego.crearConexion()));

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
        Connection conn = null;
        try {
            conn = gestorGrupoDeJuego.crearConexion();
            conn.setAutoCommit(false);

            int idGrupoJuego = guardarGrupoJuego(entity, conn);

            guardarListaJugadores(entity, idGrupoJuego, conn);

            conn.commit();
            return entity;

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.err.println("Error: " + e.getMessage());

        }
        return null;

    }

    private int guardarGrupoJuego(GrupoJuego grupoJuego, Connection con) throws SQLException {
        String sql = """
                INSERT INTO GRUPOJUEGO (NOMBREGRUPO, DESCRIPCION)
                VALUES (?, ?)
                ON DUPLICATE KEY UPDATE
                DESCRIPCION = VALUES(DESCRIPCION)
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, grupoJuego.getNombreGrupo());
            ps.setString(2, grupoJuego.getDescripcion());
            ps.executeUpdate();
        }

        String select = """
                    SELECT %s
                    FROM %s
                    WHERE NOMBREGRUPO = ?
                """.formatted("ID_GRUPO", tabla);

        PreparedStatement ps = con.prepareStatement(select);
        ps.setString(1, grupoJuego.getNombreGrupo());

        return gestorGrupoDeJuego.sacarId(ps);
    }

    private void guardarListaJugadores(GrupoJuego grupoJuego, int idGrupo, Connection con) throws SQLException {
        String sql = """
                INSERT INTO JUGADOR_GRUPOJUEGO (ID_JUGADOR, ID_GRUPO)
                VALUES (?, ?)
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int idJugador : grupoJuego.getListaMiembros()) {
                ps.setInt(1, idJugador);
                ps.setInt(2, idGrupo);
                ps.executeUpdate();
            }
        }
    }


    /**
     * Busca todos los grupos de juego que contengan al jugador pasado como parámetro
     *
     * @param idJugadorSeleccionado Id del jugador a buscar en los grupos
     * @return Devuelve los grupos de los jugadores pasados como parámetros
     */
    public List<GrupoJuego> buscarGruposPorIdJugador(int idJugadorSeleccionado) {
        return gestorGrupoDeJuego.findAllToList(GestorDeParseadores.parseadorGrupoJuego(gestorGrupoDeJuego.crearConexion())).stream()
                .filter(grupo -> grupo.getListaMiembros().contains(idJugadorSeleccionado))
                .toList();
    }
}
