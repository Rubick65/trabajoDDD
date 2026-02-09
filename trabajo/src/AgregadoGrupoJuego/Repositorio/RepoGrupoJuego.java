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
     * Guarda los datos en la base de datos
     *
     * @param entity Entidad a guardar
     * @param <S>    Grupo a guardar
     * @return Devuelve la entidad que se ha guardado
     * @throws IOException Lanza excepción en caso de fallar a la hora de leer o escribir los datos
     */
    @Override
    public <S extends GrupoJuego> S save(S entity) throws IOException {
        // Conexión para la base de datos
        Connection conn = null;
        try {
            // Crea la conexión a la base de datos
            conn = gestorGrupoDeJuego.crearConexion();
            // Indica que no se guarden los cambios automáticamente
            conn.setAutoCommit(false);

            // Guarda el grupo de juego en la base de datos y devuelve su id
            int idGrupoJuego = guardarGrupoJuego(entity, conn);

            // Guarda el id del grupo de juego relacionandolo con todos sus jugadores
            guardarListaJugadores(entity, idGrupoJuego, conn);

            // En caso de que todo salga correcto se guardan los cambios
            conn.commit();
            // Y se devuelve la entidad guardada
            return entity;

        } catch (SQLException e) {
            try {
                // En caso de que salte un error, se eliminan todos los cambios
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.err.println("Error: " + e.getMessage());

        }
        //  Y se devuelve null
        return null;

    }

    private int guardarGrupoJuego(GrupoJuego grupoJuego, Connection con) throws SQLException {
        // Sentencia para insertar los datos en la base de datos si no existían antes, en caso de existir se actualizan
        String sql = """
                INSERT INTO GRUPOJUEGO (NOMBREGRUPO, DESCRIPCION)
                VALUES (?, ?)
                ON DUPLICATE KEY UPDATE
                DESCRIPCION = VALUES(DESCRIPCION)
                """;

        // Perpara la sentencia
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            // Indica los atributos faltantes
            ps.setString(1, grupoJuego.getNombreGrupo());
            ps.setString(2, grupoJuego.getDescripcion());
            // Y ejecuta la sentencia
            ps.executeUpdate();
        }

        // Select para sacar el último id
        String select = """
                    SELECT %s
                    FROM %s
                    WHERE NOMBREGRUPO = ?
                """.formatted("ID_GRUPO", tabla);

        PreparedStatement ps = con.prepareStatement(select);
        ps.setString(1, grupoJuego.getNombreGrupo());
        // Devuelve el último id introducido
        return gestorGrupoDeJuego.sacarId(ps);
    }

    private void guardarListaJugadores(GrupoJuego grupoJuego, int idGrupo, Connection con) throws SQLException {
        // Sentencia para guardar la lista de jugadores
        String sql = """
                INSERT INTO JUGADOR_GRUPOJUEGO (ID_JUGADOR, ID_GRUPOJUEGO)
                VALUES (?, ?)
                """;

        // Se prepara la sentencia
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            // Y se guarda los datos en la tabla
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
