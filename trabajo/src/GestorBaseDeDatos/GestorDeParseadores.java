package GestorBaseDeDatos;

import AgregadoAventura.Aventura;
import AgregadoAventura.AventuraAccion;
import AgregadoAventura.AventuraMisterio;
import AgregadoGrupoJuego.GrupoJuego;
import AgregadoJugador.DireccionJuego;
import AgregadoJugador.DirectorDeJuego;
import AgregadoJugador.Jugador;
import AgregadoPersonaje.ObjetoInventario;
import AgregadoPersonaje.Personaje;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GestorDeParseadores {

    private static Connection conexion; //Conexion a la BD

    public GestorDeParseadores(Connection conexion) {
        this.conexion = conexion;
    }

    public static Function<ResultSet,Aventura> parseadorAventura() {
        return rs -> {
            try {
                Aventura aventura = new Aventura(
                        rs.getString("nombreAventura"),
                        rs.getInt("duracionSesionesAprox"),
                        Aventura.Dificultad.valueOf(rs.getString("dificultad")));

                //Como no esta en el constructor, se obtiene con set
                aventura.setID_AVENTURA(rs.getInt("ID_AVENTURA"));
                return aventura;

            } catch (SQLException e) {
                throw new RuntimeException("Error al parsear aventura", e);
            }
        };
    }

    public static Function<ResultSet, AventuraAccion> parseadorAventuraAccion() {
        return rs -> {
            try {
                AventuraAccion aventuraAccion = new AventuraAccion(
                        rs.getString("nombreAventura"),
                        rs.getInt("duracionSesionesAprox"),
                        Aventura.Dificultad.valueOf(rs.getString("dificultad")),
                        rs.getInt("cantidadEnemigos"),
                        rs.getInt("cantidadUbicaciones")
                );

                //Como no esta en el constructor, se obtiene con set nuevamente
                aventuraAccion.setID_AVENTURA(rs.getInt("ID_AVENTURA"));
                return aventuraAccion;

            } catch (SQLException e) {
                throw new RuntimeException("Error al parsear aventura Accion", e);
            }
        };
    }

    public static Function<ResultSet, AventuraMisterio> parseadorAventuraMisterio() {
        return rs -> {
            try {
                AventuraMisterio aventuraMisterio = new AventuraMisterio(
                        rs.getString("nombreAventura"),
                        rs.getInt("duracionSesionesAprox"),
                        Aventura.Dificultad.valueOf(rs.getString("dificultad")),
                        rs.getString("enigmaPrincipal")
                );

                //Igualmente se obtiene con el set.
                aventuraMisterio.setID_AVENTURA(rs.getInt("ID_AVENTURA"));
                return aventuraMisterio;

            } catch (SQLException e) {
                throw new RuntimeException("Error al parsear aventura misterio", e);
            }
        };
    }

    public Function<ResultSet, DireccionJuego> parseadorDireccionJuego() {
        return rs -> {
            try {
                DireccionJuego d = new DireccionJuego(
                        rs.getString("ciudad"),
                        rs.getString("calle"),
                        rs.getString("piso"),
                        rs.getString("codigoPostal")
                );
                return d;
            }
            catch (SQLException e) {
                throw new RuntimeException("Error al parsear DireccionJuego", e);
            }
        };
    }

    public Function<ResultSet, GrupoJuego> parseadorGrupoJuego() {
        return rs -> {
            try {
                int idGrupo = rs.getInt("ID_GRUPO");
                List<Integer> miembros = new ArrayList<>();

                // creamos select para los id de los miembros
                try (PreparedStatement ps = conexion.prepareStatement("SELECT ID_JUGADOR FROM Jugador WHERE ID_GRUPO = ?")) {
                    ps.setInt(1, idGrupo);
                    try (ResultSet rs2 = ps.executeQuery()) {
                        while (rs2.next()) {
                            miembros.add(rs2.getInt("ID_JUGADOR"));
                        }
                    }
                }
                try {
                    return new GrupoJuego(
                            rs.getString("nombreGrupo"),
                            rs.getString("descripcion"),
                            miembros
                    );
                } catch (IOException i) {
                    throw new RuntimeException("error al parsear GrupoJuego", i);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al parsear GrupoJuego", e);
            }
        };
    }

    public Function<ResultSet, Jugador> parseadorJugador() {
        return rs -> {
            try {
                DireccionJuego direccion = null;
                int idDireccion = rs.getInt("ID_DIRECCION");

                //Si no es nulo se buscan los datos de direccionJuego
                if (!rs.wasNull() && idDireccion > 0) {
                    try (PreparedStatement ps = conexion.prepareStatement("SELECT * FROM DireccionJuego WHERE ID_DIRECCION = ?")) {
                        ps.setInt(1, idDireccion);
                        try (ResultSet rs2 = ps.executeQuery()) {
                            if (rs2.next()) {
                                //Se parsea la direccion reutilizando codigo
                                direccion = parseadorDireccionJuego().apply(rs2);
                            }
                        }
                    }
                }
                Jugador jugador = new Jugador(
                        rs.getString("DNI"),
                        rs.getString("nombre"),
                        direccion
                );
                jugador.setID_JUGADOR(rs.getInt("ID_JUGADOR"));
                return jugador;
            } catch (SQLException e) {
                throw new RuntimeException("Error al parsear Jugador", e);
            }
        };
    }

    public Function<ResultSet, DirectorDeJuego> parseadorDirectorDeJuego() {
        return rs -> {
            try {
                DireccionJuego direccion = null;
                int idDireccion = rs.getInt("ID_DIRECCION");

                //Si no es nulo, se sigue
                if (!rs.wasNull() && idDireccion > 0) {
                    try (PreparedStatement ps = conexion.prepareStatement("SELECT * FROM DireccionJuego WHERE ID_DIRECCION = ?")) {
                        ps.setInt(1, idDireccion);
                        //Por cada direccion, se parsea.
                        try (ResultSet rs2 = ps.executeQuery()) {
                            //Si tiene una direccion, se parsea, no hace falta bucle al tener solo 1.
                            if (rs2.next()) {
                                direccion = parseadorDireccionJuego().apply(rs2);
                            }
                        }
                    }
                }
                List<Integer> listaAventuras = new ArrayList<>();
                int idDirector = rs.getInt("ID_JUGADOR");

                try (PreparedStatement ps = conexion.prepareStatement("SELECT ID_AVENTURA FROM DirectorAventura WHERE ID_DIRECTOR = ?")) {
                    ps.setInt(1, idDirector);
                    try (ResultSet rs3 = ps.executeQuery()) {
                        while (rs3.next()) {
                            listaAventuras.add(rs3.getInt("ID_AVENTURA"));
                        }
                    }
                }
                DirectorDeJuego director = new DirectorDeJuego(
                        rs.getString("DNI"),
                        rs.getString("nombre"),
                        direccion,
                        listaAventuras
                );
                director.setID_JUGADOR(idDirector);
                return director;
            } catch (SQLException e) {
                throw new RuntimeException("Error al parsear DirectorDeJuego", e);
            }
        };
    }

    public Function<ResultSet, Personaje> parseadorPersonaje() {
        return rs -> {
            try {
                int idPersonaje = rs.getInt("ID_PERSONAJE");
                int idJugador = rs.getInt("ID_JUGADOR");

                // cargamos el inventario
                List<ObjetoInventario> inventario = new ArrayList<>();
                try (PreparedStatement ps = conexion.prepareStatement("SELECT * FROM ObjetoInventario WHERE ID_PERSONAJE = ?")) {
                    ps.setInt(1, idPersonaje);

                    try (ResultSet rs2 = ps.executeQuery()) {
                        while (rs2.next()) {
                            ObjetoInventario objeto = new ObjetoInventario(
                                    rs2.getString("nombre"),
                                    rs2.getDouble("peso"),
                                    rs2.getString("descripcionObjeto")
                            );
                            inventario.add(objeto);
                        }
                    }
                }
                //Se parsean a Enum los datos necesarios
                Personaje.Clase clase = Personaje.Clase.valueOf(rs.getString("clase"));
                Personaje.Raza raza = Personaje.Raza.valueOf(rs.getString("raza"));

                Personaje personaje = new Personaje(
                        idJugador,
                        inventario,
                        rs.getDouble("capacidadCarga"),
                        rs.getString("nombrePersonaje"),
                        rs.getString("descripcion"),
                        rs.getString("historia"),
                        clase,
                        raza
                );
                personaje.setID_PERSONAJE(idPersonaje);
                return personaje;
            } catch (SQLException e) {
                throw new RuntimeException("Error al parsear Personaje", e);
            }
        };
    }

    public Function<ResultSet, ObjetoInventario> parseadorObjetoInventario() {
        return rs -> {
            try {
                ObjetoInventario objeto = new ObjetoInventario(
                        rs.getString("nombre"),
                        rs.getDouble("peso"),
                        rs.getString("descripcionObjeto")
                );
                return objeto;
            } catch (SQLException e) {
                throw new RuntimeException("Error al parsear ObjetoInventario", e);
            }
        };
    }
}
