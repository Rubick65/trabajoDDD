package AgregadoPersonaje.Repositorio;

import AgregadoPersonaje.ObjetoInventario;
import AgregadoPersonaje.Personaje;
import GestorBaseDeDatos.GestorDB;
import GestorBaseDeDatos.GestorDeParseadores;
import Interfaces.IRepositorioExtend;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class RepoPersonaje implements IRepositorioExtend<Personaje, Integer> {


    private final GestorDB gestorPersonajes;
    private final String nombreID = "ID_PERSONAJE";
    private final String tabla = "Personaje";

    /**
     * Se cargan los datos del json
     *
     * @throws IOException si ocurre un error al cargar
     */
    public RepoPersonaje() throws IOException {
        this.gestorPersonajes = new GestorDB(tabla);
    }

    /**
     * Se buscan los personajes por su clase
     *
     * @param clase a buscar
     * @return lista filtrada por clases
     */
    public List<Personaje> buscarPersonajesPorClases(Personaje.Clase clase) {
        return gestorPersonajes.buscarPorDato(clase, GestorDeParseadores.parseadorPersonaje(this.gestorPersonajes.crearConexion()), "clase");
    }

    /**
     * Se obtiene mediante optional un personaje
     *
     * @param id a buscar
     * @return personaje a buscar
     */
    @Override
    public Optional<Personaje> findByIdOptional(Integer id) throws IOException {
        return Optional.ofNullable(gestorPersonajes.findById(id, nombreID, GestorDeParseadores.parseadorPersonaje(this.gestorPersonajes.crearConexion())));
    }

    /**
     * Se obtiene una lista de todos los personajes
     *
     * @return lista de personajes
     */
    @Override
    public List<Personaje> findAllToList() throws IOException {
        return gestorPersonajes.findAllToList(GestorDeParseadores.parseadorPersonaje(this.gestorPersonajes.crearConexion()));
    }

    /**
     * Se cuenta la cantidad de personajes
     *
     * @return cantidad
     * @throws IOException si no hay personajes
     */
    @Override
    public long count() throws IOException {
        return gestorPersonajes.count();
    }

    /**
     * Se elimina un personaje mediante su id
     *
     * @param id del personaje a eliminar
     * @throws IOException si no existe el id
     */
    @Override
    public void deleteById(Integer id) throws IOException {
        gestorPersonajes.deleteById(id, nombreID);
    }

    /**
     * Se eliminan todos los personajes
     *
     * @throws IOException si ocurre un error al guardar
     */
    @Override
    public void deleteAll() throws IOException {
        gestorPersonajes.deleteAll();
    }

    /**
     * Se comprueba la existencia de un personaje mediante su id
     *
     * @param id a comprobar
     * @return si existe o no
     */
    @Override
    public boolean existsById(Integer id) throws IOException {
        return gestorPersonajes.existById(id, nombreID);
    }

    /**
     * Se obtiene un personaje por su id
     *
     * @param id a buscar
     * @return personaje con ese id
     * @throws IOException si no existe ese id
     */
    @Override
    public Personaje findById(Integer id) throws IOException {
        return gestorPersonajes.findById(id, nombreID, GestorDeParseadores.parseadorPersonaje(gestorPersonajes.crearConexion()));

    }

    /**
     * Se obtiene la lista de personajes mediante iterable
     *
     * @return lista de personajes
     */
    @Override
    public Iterable<Personaje> findAll() throws IOException {
        return gestorPersonajes.findAllToList(GestorDeParseadores.parseadorPersonaje(this.gestorPersonajes.crearConexion()));
    }

    /**
     * Se guardan los datos en la base de datos
     *
     * @param entity personaje a actualizar o guardar
     * @param <S>    entidad e hijos
     * @return devuelve el personaje actualizado o guardado
     */
    @Override
    public <S extends Personaje> S save(S entity) {
        int idPersonaje = 0;
        try (Connection con = gestorPersonajes.crearConexion()) {
            // Se quita el autocommit
            con.setAutoCommit(false);

            idPersonaje = guardarPersonaje(entity, con);

            guardarObjetosInventario(entity.getInventario(), con, idPersonaje);

            con.setAutoCommit(true);
            con.commit();
            return entity;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * Guarda un personaje en la base de datos
     *
     * @param entity Personaje a guardar
     * @param con    Conexión con la base de datos
     * @return Devuelve el id del personaje recien guardado
     * @throws SQLException lanza exepción en caso de error durante el guardado
     */
    private int guardarPersonaje(Personaje entity, Connection con) throws SQLException {
        // Insert o Update para guardar o actualizar datos en la base de datos
        String insert = "INSERT INTO " + this.tabla + " (ID_JUGADOR, CAPACIDADCARGA, NOMBREPERSONAJE, DESCRIPCION, HISTORIA, CLASE, RAZA) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) AS nuevo " +
                "ON DUPLICATE KEY UPDATE " +
                "capacidadCarga = nuevo.capacidadCarga, " +
                "nombrePersonaje = nuevo.nombrePersonaje, " +
                "descripcion = nuevo.descripcion, " +
                "historia = nuevo.historia, " +
                "clase = nuevo.clase, " +
                "raza = nuevo.raza";

        // En caso de que si se haya podido guardar los objetos preparamos el insert del personaje
        try (PreparedStatement ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            // Se añaden todos los valores del personaje a insertar o actualizar
            ps.setInt(1, entity.getID_JUGADOR());
            ps.setDouble(2, entity.getCapacidadCarga());
            ps.setString(3, entity.getNombrePersonaje());
            ps.setString(4, entity.getDescripcion());
            ps.setString(5, entity.getHistoria());
            ps.setString(6, entity.getClase().toString());
            ps.setString(7, entity.getRaza().toString());
            ps.executeUpdate();

        } catch (SQLException e) {
            con.rollback();
            throw new SQLException("Error al insertar el personaje " + e.getMessage());
        }

        // Select para sacar el ID del personaje recién guardado
        String select = """
                    SELECT %s
                    FROM %s
                    WHERE nombrePersonaje = ?
                    AND descripcion = ?
                """.formatted(nombreID, this.tabla);

        // Preparamos el select
        PreparedStatement ps = con.prepareStatement(select);
        ps.setString(1, entity.getNombrePersonaje());
        ps.setString(2, entity.getDescripcion());

        // Devolvemos el id del personaje
        return gestorPersonajes.sacarId(ps);
    }

    /**
     * Guarda los objetos del inventario del personaje
     *
     * @param objetosInventario Lista de objetos del inventario
     * @param con               Conexión a la base de datos
     * @param id_personaje      Id del personaje al que pertenece el objeto
     * @throws SQLException Lanza una excepción en caso de error durante el insert
     */
    private void guardarObjetosInventario(List<ObjetoInventario> objetosInventario, Connection con, int id_personaje) throws SQLException {
        String insertObjetos = insertTablaObjetosInventario();

        // Quitamos el autoCommit
        con.setAutoCommit(false);
        for (ObjetoInventario obj : objetosInventario) {
            // Preparamos el insert o update de los objetos del inventario
            try (PreparedStatement psO = con.prepareStatement(insertObjetos)) {
                // Indicamos los valores a guardar
                psO.setInt(1, id_personaje);
                psO.setString(2, obj.getNombre());
                psO.setString(3, obj.getDescripcionObjeto());
                psO.setDouble(4, obj.getPeso());
                psO.executeUpdate();

            } catch (SQLException e) {
                // Damos marcha atrás
                con.rollback();
                // En caso de error
                throw new SQLException("No se ha podido guardar los objetos: " + e.getMessage());
            }
        }
    }

    /**
     * Crea el insert a la tabla de objetos
     *
     * @return Devuelve un string con el insert a la tabla de objetos
     */
    private String insertTablaObjetosInventario() {
        // Tabla para guardar los objetos del inventario
        String tablaObjetos = "ObjetoInventario";

        // Insert o update para guardar o actualizar los objetos del inventario del jugador
        return "INSERT INTO " + tablaObjetos + " (ID_PERSONAJE, NOMBRE, DESCRIPCIONOBJETO, PESO) " +
                "VALUES (?, ?, ?, ?) AS fila_nueva " +
                "ON DUPLICATE KEY UPDATE " +
                "NOMBRE = fila_nueva.NOMBRE, " +
                "DESCRIPCIONOBJETO = fila_nueva.DESCRIPCIONOBJETO, " +
                "PESO = fila_nueva.PESO";
    }
}

