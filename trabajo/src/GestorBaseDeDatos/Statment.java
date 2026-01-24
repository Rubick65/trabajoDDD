package GestorBaseDeDatos;

import AgregadoJugador.DireccionJuego;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static GestorBaseDeDatos.GestorDeParseadores.parseadorDireccionJuego;

public class Statment {

    public static void aniadirAListaEnteros(Connection conexion, List<Integer> listaAniadir, String idBuscar, String tabla, String idCondicion, int valorCondicion) {
        try (PreparedStatement ps = conexion.prepareStatement("SELECT " + idBuscar + " FROM " + tabla + " WHERE " + idCondicion + " = ?")) {
            ps.setInt(1, valorCondicion);

            try (ResultSet rs2 = ps.executeQuery()) {
                while (rs2.next()) {
                    //Se obtiene los id de los jugadores
                    listaAniadir.add(rs2.getInt(idBuscar));
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DireccionJuego obtenerDireccionDeJuego(Connection conexion, int idBuscar) {
        DireccionJuego direccion = null; //Quitar del otro

        try (PreparedStatement ps = conexion.prepareStatement("SELECT * FROM DireccionJuego WHERE ID_DIRECCION = ?")) {
            ps.setInt(1, idBuscar);
            try (ResultSet rs2 = ps.executeQuery()) {
                if (rs2.next()) {
                    direccion = parseadorDireccionJuego(conexion).apply(rs2);
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return direccion;
    }

    public static boolean existeEn(Connection conexion,String tablaBuscar, String idBuscar ,int idCondicion) {
        String sql = "SELECT 1 FROM " + tablaBuscar + " WHERE " + idBuscar + " = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idCondicion);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
