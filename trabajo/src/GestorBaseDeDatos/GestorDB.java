package GestorBaseDeDatos;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

public class GestorDB {

    // Nombre de la tabla a la que se hace referencia
    private String tabla;

    /**
     * Se indica el valor por defecto de la tabla
     *
     * @param tabla Nombre de la tabla para la conexión con la base de datos
     */
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
    public Connection crearConexion() {
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
        try {
            return DriverManager.getConnection("jdbc:mysql://" + servidor + ":" + puerto + "/" + nombreBD, connectionProps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
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
     * @return Devuelve true si existe y false en caso contrario
     */
    public boolean existById(Integer idObjetivo, String nombreId) {
        // Conexión
        try (Connection conn = crearConexion()) {
            String select = "SELECT 1 FROM " + this.tabla + " WHERE " + nombreId + " = ? LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(select);
            ps.setInt(1, idObjetivo);

            ResultSet rs = ps.executeQuery();
            return rs.next(); // true si hay al menos un registro

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
    public void deleteAll() {
        // Conexión
        try (Connection conn = crearConexion()) {

            // Delete de todos los objetos de una tabla
            String delete = "DELETE FROM " + this.tabla;

            // Quitamos el auto commit
            conn.setAutoCommit(false);
            // Se prepara el delete
            try (PreparedStatement ps = conn.prepareStatement(delete)) {
                // Y se ejecuta el delete
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                try {
                    conn.rollback();
                } catch (SQLException _) {
                }
            } finally {
                //insertar codigo para cerrar todo
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.err.println("Error a la hora de realizar la conexión" + e.getMessage());
        }
    }

    /**
     * Elimina una entrada en la tabla
     *
     * @param idObjetivo Id de la entrada a eliminar
     * @param nombreId   Nombre del id en la tabla
     * @return Devuelve la cantidad de elementos eliminados o -1 en caso de error
     */
    public void deleteById(Integer idObjetivo, String nombreId) throws IOException {
        // Conexión
        try (Connection conn = crearConexion()) {
            // Se crea el select
            String delete = "DELETE FROM " + this.tabla + " WHERE " + nombreId + " = ?";

            // Quitamos el auto commit
            conn.setAutoCommit(false);
            // Se prepara el delete
            try (PreparedStatement ps = conn.prepareStatement(delete)) {

                // Se añade el parámetro faltante
                ps.setInt(1, idObjetivo);
                // Y se ejecuta el delete
                int eliminados = ps.executeUpdate();

                if (eliminados == 0)
                    throw new IOException();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                try {
                    conn.rollback();
                } catch (SQLException excep) {
                    //código adicional
                }
            } catch (IOException e) {
                throw new IOException();
            } finally {
                //insertar codigo para cerrar todo
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.err.println("Error a la hora de realizar la conexión" + e.getMessage());
        }
    }


    public <R> R findById(Integer idObjetivo, String nombreId, Function<ResultSet, R> parseador) {
        // Conexión
        try (Connection conn = crearConexion()) {
            // Se crea el select
            String select = "SELECT * FROM " + this.tabla + " WHERE " + nombreId + " = ?";

            // Se prepara el select y se añade el parámetro faltante
            PreparedStatement ps = conn.prepareStatement(select);
            ps.setInt(1, idObjetivo);

            // Se ejecuta el select
            ResultSet rs = ps.executeQuery();

            // Si existe el next
            if (rs.next()) {
                return parseador.apply(rs);
            } else {
                // En caso contrario se devuelve un nulo
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Devuelve una lista con los datos parseados de los objetos
     *
     * @param parseador Función que parseará los datos de
     * @param <R>       Dato genérico para él parseo
     * @return Devuelve la lista con los datos parseados
     * @throws SQLException Lanza excepción si ocurre algún error con la conexión
     */
    public <R> List<R> findAllToList(Function<ResultSet, R> parseador) {
        // Lista de objetos a devolver
        List<R> listaObjetos = new ArrayList<>();
        // Select para buscar todos los datos de la tabla
        String sql = "SELECT * FROM " + this.tabla;
        // Se crea la conexión se prepara el el select y se ejecuta
        try (
                Connection conn = crearConexion();
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery(sql)
        ) {
            // Mientas que existan datos
            while (rs.next()) {
                // Añadimos el siguiente objeto a la lista de objetos
                listaObjetos.add(parseador.apply(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Se devuelve la lista
        return listaObjetos;
    }

    /**
     * Busca un dato en cualquier tabla
     *
     * @param dato           Dato a buscar
     * @param parseador      Parseador para sacar el objeto
     * @param nombreObjetivo Nombre de la fila a buscar
     * @param <R>            Dato genérico
     * @return Devuelve la lista con los datos
     * @throws SQLException Lanza excepción cuando aparece un problema con la conexión a la a base de datos
     */
    public <R, T> List<T> buscarPorDato(R dato, Function<ResultSet, T> parseador, String nombreObjetivo) {
        List<T> listaDeDatos = new ArrayList<>();
        try (Connection conexion = crearConexion()) {
            // Select ha realizar
            String select = "select * from " + this.tabla + " where " + nombreObjetivo + " = ?";
            // Se prepara el select
            PreparedStatement ps = conexion.prepareStatement(select);
            // Se indica el tipo de dato faltante y se pone
            sacarPorTipoDeDato(ps, dato);

            // Se ejecuta el select
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Se va añadiendo los datos parseados a la lista
                listaDeDatos.add(parseador.apply(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Se devuelve la lista de datos
        return listaDeDatos;
    }

    /**
     * Pone el tipo de dato necesario para el select
     *
     * @param ps   Select para poner el dato
     * @param dato Dato a parsear
     * @param <R>  Dato genérico
     * @throws SQLException Lanza una excepción en caso de problemas con el select
     */
    private <R> void sacarPorTipoDeDato(PreparedStatement ps, R dato) throws SQLException {
        // En función del tipo de dato
        switch ((Object) dato) {
            // Se prepara de una manera u otra el select
            case String s -> ps.setString(1, s);
            case Integer i -> ps.setInt(1, i);
            case Double d -> ps.setDouble(1, d);
            case Enum e -> ps.setString(1, e.toString());
            default -> throw new IllegalStateException("Unexpected value: " + dato);
        }
    }

    public int sacarId(PreparedStatement ps) {
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
