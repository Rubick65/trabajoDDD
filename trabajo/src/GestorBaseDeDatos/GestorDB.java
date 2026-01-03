package GestorBaseDeDatos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

public class GestorDB {

    private String tabla;

    public GestorDB(String tabla) {
        setTabla(tabla);
    }


    // Getters y Setters del atributo
    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    /**
     * Crea la conexión con la base de datos
     *
     * @return Connection devuelve la conexión a la base de datos
     * @throws SQLException Lanza una excepción en caso de problemas con la conexión
     */
    public Connection crearConexion() throws SQLException {
        // Datos de conexión
        String servidor = "localhost";
        String puerto = "3306";
        String nombreBD = "TRABAJODDD";
        String usuario = "root";
        String contrasenna = "root";

        // Creación de propiedades para la conexión
        Properties connectionProps = new Properties();
        connectionProps.setProperty("user", usuario);
        connectionProps.setProperty("password", contrasenna);
        connectionProps.setProperty("serverTimezone", "UTC");

        // Devuelve la conexión a la base de datos para ser usada
        return DriverManager.getConnection("jdbc:mysql://" + servidor + ":" + puerto + "/" + nombreBD, connectionProps);
    }

    /**
     * Cuenta la cantidad de elementos en una tabla
     *
     * @return Devuelve la cantidad de elementos existente
     */
    public int count() {
        // Conexión
        try (Connection conn = crearConexion()) {
            // Select ha realizar
            String select = "select COUNT(*) from " + this.tabla;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(select);

            // Devolvemos el resultado del count
            return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error a la hora de realizar la conexión" + e.getMessage());
            return -1;
        }
    }

    /**
     * Comprueba si un id existe en la base de datos
     *
     * @param idObjetivo Id a comprobar
     * @param nombreId   nombre del id en la tabla
     * @return
     */
    public boolean existById(Integer idObjetivo, String nombreId) {
        // Conexión
        try (Connection conn = crearConexion()) {

            // Se crea el select
            String sql = "SELECT 1 FROM " + this.tabla + " WHERE " + nombreId + " = ?";

            // Se prepara el select y se añade el parámetro faltante
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idObjetivo);

            // Se ejecuta el select
            ResultSet rs = ps.executeQuery();

            // Devuelve un boolean que indica la existencia del objetivo
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Error a la hora de realizar la conexión" + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina todos los datos de una tabla
     *
     * @return Devuelve 0 si no pudo eliminar nada, 1 si sí se eliminó correctamente y -1 si ocurrió algún error en el
     * proceso
     */
    public int deleteAll() {
        // Conexión
        try (Connection conn = crearConexion()) {

            // Delete de todos los objetos de una tabla
            String delete = "DELETE FROM " + this.tabla;

            // Se crea el delete
            try (Statement st = conn.createStatement()) {
                // Y se ejecuta el delete
                return st.executeUpdate(delete);
            }

        } catch (SQLException e) {
            System.err.println("Error a la hora de realizar la conexión" + e.getMessage());
            return -1;
        }
    }

    /**
     * Elimina un
     * @param idObjetivo
     * @param nombreId
     * @return
     */
    public int deleteById(Integer idObjetivo, String nombreId) {
        // Conexión
        try (Connection conn = crearConexion()) {
            // Se crea el select
            String delete = "DELETE FROM " + this.tabla + " WHERE " + nombreId + " = ?";

            // Se prepara el delete
            try (PreparedStatement ps = conn.prepareStatement(delete)) {
                // Se añade el parámetro faltante
                ps.setInt(1, idObjetivo);
                // Y se ejecuta el delete
                return ps.executeUpdate(delete);
            }

        } catch (SQLException e) {
            System.err.println("Error a la hora de realizar la conexión" + e.getMessage());
            return -1;
        }

    }

    public <R> List<R> findAllToList(Function<String[], R> parseador) throws SQLException {
        List<R> listaObjetos = new ArrayList<>();
        String sql = "SELECT * FROM " + tabla;
        try (
                Connection conn = crearConexion();
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery(sql)
        ) {
            //Gracias a meta obtenemos todos los datos de la
            // tabla de donde se ha realizado el select, y sacamos en este caso la cantidad de columnas.
            ResultSetMetaData meta = rs.getMetaData();
            int columnas = meta.getColumnCount();
            while (rs.next()) {
                String[] datos = new String[columnas];
                for (int i = 1; i <= columnas; i++) {
                    datos[i - 1] = rs.getString(i);
                }
                listaObjetos.add(parseador.apply(datos));
            }
        }
        return listaObjetos;
    }
}


