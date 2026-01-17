package GestorBaseDeDatos;

import AgregadoJugador.DireccionJuego;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static GestorBaseDeDatos.GestorDeParseadores.parseadorDireccionJuego;

public class Statment {

    public static void aniadirAListaEnteros(Connection conexion, List<Integer> listaAniadir, String IdBuscar, String tabla, String IdCondicion, int valorCondicion) {
        try (PreparedStatement ps = conexion.prepareStatement("SELECT " + IdBuscar + " FROM " + tabla + " WHERE " + IdCondicion + " = ?")) {
            ps.setInt(1, valorCondicion);

            try (ResultSet rs2 = ps.executeQuery()) {
                while (rs2.next()) {
                    //Se obtiene los id de los jugadores
                    listaAniadir.add(rs2.getInt(IdBuscar));
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DireccionJuego parsearDireccionJuego(Connection conexion, int IdBuscar) {
        DireccionJuego direccion = null; //Quitar del otro

        try (PreparedStatement ps = conexion.prepareStatement("SELECT * FROM DireccionJuego WHERE ID_DIRECCION = ?")) {
            ps.setInt(1, IdBuscar);
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
}
