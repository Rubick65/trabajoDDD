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
        Connection conn = null;
        try {
            conn = gestorJugador.crearConexion();
            conn.setAutoCommit(false);

            int idDireccion = guardarDireccion(entity.getDireccionJuego(), conn);
            int idJugador = guardarJugador(entity, idDireccion, conn);

            if (entity instanceof DirectorDeJuego dj)
                guardarDirectorDeJuego(dj, idJugador, conn);

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

    private int guardarDireccion(DireccionJuego d, Connection conn) throws SQLException {


        String sql = """
                    INSERT INTO DireccionJuego (CIUDAD, CALLE, PISO, CODIGOPOSTAL)
                    VALUES (?, ?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                    CIUDAD = VALUES(CIUDAD),
                    CALLE = VALUES(CALLE),
                    PISO = VALUES(PISO),
                    CODIGOPOSTAL = VALUES(CODIGOPOSTAL)
                """;

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

        return gestorJugador.sacarId(ps);
    }

    private int guardarJugador(Jugador jugador, int idDireccion, Connection conn) throws SQLException {
        String sql = """
                    INSERT INTO Jugador (DNI, nombre, ID_DIRECCION)
                    VALUES (?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                    nombre = VALUES(nombre),
                    ID_DIRECCION = VALUES(ID_DIRECCION)
                """;

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

        return gestorJugador.sacarId(ps);
    }


    private void guardarDirectorDeJuego(DirectorDeJuego directorDeJuego, int idJugador, Connection conn) throws SQLException {
        String sql = """
                    INSERT INTO DirectorDeJuego (ID_JUGADOR, ID_AVENTURA)
                    VALUES (?, ?)
                    ON DUPLICATE KEY UPDATE
                    ID_AVENTURA = VALUES(ID_AVENTURA)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idJugador);
            ps.setInt(2, directorDeJuego.getAventuraSeleccionada());
            ps.executeUpdate();
        }

        if (!directorDeJuego.getListaAventuras().isEmpty())
            guardarListaAventuras(directorDeJuego, idJugador, conn);

    }


    private void guardarListaAventuras(DirectorDeJuego directorDeJuego, int idJugador, Connection conn) throws SQLException {
        String sql = """
                INSERT INTO DIRECTORAVENTURA (ID_DIRECTOR, ID_AVENTURA)
                VALUES (?, ?)
                """;

        for (int id_aventura : directorDeJuego.getListaAventuras()) {
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
        return findAllToList().stream().filter(jugador -> jugador.getDireccionJuego().getCalle().equalsIgnoreCase(calle)).toList();
    }

}