package AgregadoJugador.Repositorio;

import AgregadoJugador.DireccionJuego;
import AgregadoJugador.DirectorDeJuego;
import AgregadoJugador.Jugador;
import GestorBaseDeDatos.GestorDB;
import GestorBaseDeDatos.GestorDeParseadores;
import Interfaces.IRepositorioExtend;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RepoJugador implements IRepositorioExtend<Jugador, Integer> {

    private final GestorDB gestorJugador;
    private final String nombreId = "ID_JUGADOR";
    private final String tabla = "Jugador";


    /**
     * Constructor que saca los datos del fichero
     *
     * @throws IOException Lanza una excepción en caso de no haber encontrado el fichero
     */
    public RepoJugador() {
        gestorJugador = new GestorDB(tabla);
    }

    /**
     * Busca en la lista de jugadores por el id introducido
     *
     * @param id Id introducido por el usuario
     * @return En caso de existir el jugador devuelve ese jugador, en caso contrario Optional.empty()
     */
    @Override
    public Optional<Jugador> findByIdOptional(Integer id) throws IOException {
        return Optional.ofNullable(gestorJugador.findById(id, nombreId, GestorDeParseadores.parseadorJugador(this.gestorJugador.crearConexion())));
    }

    /**
     * Devuelve todos los jugadores en una lista
     *
     * @return Devuelve los jugadores en un objeto tipo List
     */
    @Override
    public List<Jugador> findAllToList() throws IOException {
        return gestorJugador.findAllToList(GestorDeParseadores.parseadorJugador(gestorJugador.crearConexion()));
    }

    /**
     * Cuenta la cantidad de jugadores presentes en la lista
     *
     * @return Devuelve la cantidad de jugadores en la lista
     */
    @Override
    public long count() throws IOException {
        return gestorJugador.count();
    }

    /**
     * Eliminina un jugdor
     *
     * @param id Id del jugador a eliminar
     * @throws IOException lanza una excepción si falla al leer el archivo
     */
    @Override
    public void deleteById(Integer id) throws IOException {
        gestorJugador.deleteById(id, nombreId);
    }

    /**
     * Elimina todos los datos del fichero
     *
     * @throws IOException Lanza la excepción cuando no se puede leer correctamente el archivoi
     */
    @Override
    public void deleteAll() throws IOException {
        gestorJugador.deleteAll();
    }

    /**
     * Comprueba si existe un jugador con el id pasado como parámetro
     *
     * @param id Id del jugador a buscar
     * @return Devuelve true en caso de que si exista y false en caso contrario
     */
    @Override
    public boolean existsById(Integer id) throws IOException {
        return gestorJugador.existById(id, nombreId);
    }

    /**
     * Busca un jugador por su id
     *
     * @param id Id del jugador a buscar
     * @return Devuelve en caso de encontrarlo el jugador buscado
     */
    @Override
    public Jugador findById(Integer id) throws IOException {
        return gestorJugador.findById(id, nombreId, GestorDeParseadores.parseadorJugador(this.gestorJugador.crearConexion()));
    }

    /**
     * Devuelve una colección con todos los jugadores
     *
     * @return Devuelve una colección con todos los jugadores
     */
    @Override
    public Iterable<Jugador> findAll() throws IOException {
        return gestorJugador.findAllToList(GestorDeParseadores.parseadorJugador(this.gestorJugador.crearConexion()));

    }

    /**
     * Guarda el jugador en la base de datos
     *
     * @param entity Entidad que se va a guardar
     * @param <S>    Objeto que extiende de jugador
     * @return Devuele la entidad guardada
     * @throws IllegalArgumentException Lanza esta excepción en caso de que la lista ya contenga a ese jugador
     * @throws IOException              Lanza este error cuando la lectura del archivo falla
     */
    @Override
    public <S extends Jugador> S save(S entity) throws IllegalArgumentException, IOException {
        // Conexión a la base de datos
        Connection conn = null;
        try {
            // Crea la conexión a la base de datos
            conn = gestorJugador.crearConexion();
            // Se quita el autoguardado
            conn.setAutoCommit(false);

            // Guarda la dirección en la base de datos y saca el id
            int idDireccion = guardarDireccion(entity.getDireccionJuego(), conn);
            // Guarda el jugador en la base de datos y devuelve su id
            int idJugador = guardarJugador(entity, idDireccion, conn);

            // En caso de que sea un director de juego
            if (entity instanceof DirectorDeJuego dj)
                // Se guardan los datos específicos en la base de datos
                guardarDirectorDeJuego(dj, idJugador, conn);

            // Guarda los datos en la base de datos
            conn.commit();
            // Devuelve la entidad
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

    /**
     * Guarda la dirección de juego en la base de datos
     *
     * @param d    Dirección de juegeo
     * @param conn Conexión a la base de datos
     * @return Devuelve el id de la dirección guardada
     * @throws SQLException
     */
    private int guardarDireccion(DireccionJuego d, Connection conn) throws SQLException {

        // Inserta los datos de la dirección de juego en la base de datos
        String sql = """
                    INSERT INTO DireccionJuego (CIUDAD, CALLE, PISO, CODIGOPOSTAL)
                    VALUES (?, ?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                    CIUDAD = VALUES(CIUDAD),
                    CALLE = VALUES(CALLE),
                    PISO = VALUES(PISO),
                    CODIGOPOSTAL = VALUES(CODIGOPOSTAL)
                """;


        // Prepara la setencia y la ejecuta
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getCiudad());
            ps.setString(2, d.getCalle());
            ps.setString(3, d.getPiso());
            ps.setString(4, d.getCodigoPostal());

            ps.executeUpdate();

        }
        // Select para sacar el ID del jugador recién guardado
        String select = """
                    SELECT %s
                    FROM %s
                    WHERE CIUDAD = ?
                    AND CALLE = ?
                    AND PISO = ?
                """.formatted("ID_DIRECCION", "DIRECCIONJUEGO");

        // Preparamos el select
        PreparedStatement ps = conn.prepareStatement(select);
        ps.setString(1, d.getCiudad());
        ps.setString(2, d.getCalle());
        ps.setString(3, d.getPiso());

        // Devuelve el id de la última dirección guardada
        return gestorJugador.sacarId(ps);
    }

    /**
     * Guarda el jugador en la base de datos
     *
     * @param jugador     Jugador a guardar
     * @param idDireccion Id de la dirección de juego
     * @param conn        Conexión a la base de datos
     * @return Devuelve el id del jugador guardado
     * @throws SQLException
     */
    private int guardarJugador(Jugador jugador, int idDireccion, Connection conn) throws SQLException {

        // Inserta los datos del jugador en la base de datos
        String sql = """
                    INSERT INTO Jugador (DNI, nombre, ID_DIRECCION)
                    VALUES (?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                    nombre = VALUES(nombre),
                    ID_DIRECCION = VALUES(ID_DIRECCION)
                """;

        // Prepara el insert y lo ejecuta
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, jugador.getDNI());
            ps.setString(2, jugador.getNombre());
            ps.setInt(3, idDireccion);
            ps.executeUpdate();
        }

        // Select para sacar el ID del jugador recién guardado
        String select = """
                    SELECT %s
                    FROM %s
                    WHERE DNI = ?
                """.formatted(nombreId, this.tabla);

        // Preparamos el select
        PreparedStatement ps = conn.prepareStatement(select);
        ps.setString(1, jugador.getDNI());

        // Devuelve el id del último jugador guardado en la base de datos
        return gestorJugador.sacarId(ps);
    }

    /**
     * Guarda el director de juego en la base de datos
     *
     * @param directorDeJuego Director de juego a guardar
     * @param idJugador       id del jugador
     * @param conn            Conexión a la base de datosl
     * @throws SQLException
     */
    private void guardarDirectorDeJuego(DirectorDeJuego directorDeJuego, int idJugador, Connection conn) throws SQLException {
        // Guarda los datos del director de juego en la base de datos
        String sql = """
                    INSERT INTO DirectorDeJuego (ID_JUGADOR, ID_AVENTURA)
                    VALUES (?, ?)
                    ON DUPLICATE KEY UPDATE
                    ID_AVENTURA = VALUES(ID_AVENTURA)
                """;

        // Sacamos la aventura seleccionada
        int idAventuraSeleccionada = directorDeJuego.getAventuraSeleccionada();

        // Prepara el insert y lo ejecuta
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idJugador);

            ps.setObject(2, idAventuraSeleccionada != 0 ? idAventuraSeleccionada : null, java.sql.Types.INTEGER);

            ps.executeUpdate();
        }

        // En caso de que la lista de aventuras no este vacía
        if (!directorDeJuego.getListaAventuras().isEmpty())
            // Guarda la lista de aventuras
            guardarListaAventuras(directorDeJuego, idJugador, conn);

    }

    /**
     * Guarda la lista de aventuras en la base de datos
     *
     * @param directorDeJuego Director de juego
     * @param idJugador       Id del director de jeugo
     * @param conn            Conexión a la base de datos
     * @throws SQLException
     */
    private void guardarListaAventuras(DirectorDeJuego directorDeJuego, int idJugador, Connection conn) throws SQLException {
        // Crea el insert para guardar los datos en la base de datos
        String sql = """
                INSERT INTO DIRECTORAVENTURA (ID_DIRECTOR, ID_AVENTURA)
                VALUES (?, ?)
                """;

        // Recorre todos los ids de todas las aventuras
        for (int id_aventura : directorDeJuego.getListaAventuras()) {
            // Prepara el insert por cada id y guarda los datos
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idJugador);
                ps.setInt(2, id_aventura);
                ps.executeUpdate();
            }
        }
    }

    /**
     * Método añadido que se encarga de buscar jugadores por su calle
     *
     * @param calle Calle introducida por el usuario para buscar todos los jugadores que tengan esa calle como dirección de juego
     * @return Devuelve una lista con todos los jugadores que viven en la calle seleccionada
     */
    public List<Jugador> buscarJugadorPorDireccion(String calle) throws IOException {
        return findAllToList().stream().filter(jugador -> jugador.getDireccionJuego() != null && jugador.getDireccionJuego().getCalle().equalsIgnoreCase(calle)).toList();
    }
}