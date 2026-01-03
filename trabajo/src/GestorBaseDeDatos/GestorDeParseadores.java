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

    private static Connection conexion; // Conexion a la BD

    public GestorDeParseadores(Connection conexion) {
        this.conexion = conexion;
    }

    public static Function<ResultSet, Aventura> parseadorAventura() {
        return rs -> {
            try {
                Aventura aventura = new Aventura(
                        rs.getString("nombreAventura"),
                        rs.getInt("duracionSesionesAprox"),
                        Aventura.Dificultad.valueOf(rs.getString("dificultad"))
                );
                //Como no esta el ID en el constructor, se añade manualmente, igual
                //Con sus hijas.
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
                return new DireccionJuego(
                        rs.getString("ciudad"),
                        rs.getString("calle"),
                        rs.getString("piso"),
                        rs.getString("codigoPostal")
                );
            } catch (SQLException e) {
                throw new RuntimeException("Error al parsear DireccionJuego", e);
            }
        };
    }

    public Function<ResultSet, GrupoJuego> parseadorGrupoJuego() {
        return rs -> {
            try {
                int idGrupo = rs.getInt("ID_GRUPO");
                //Lista de miembros a sacar
                List<Integer> miembros = new ArrayList<>();

                try (PreparedStatement ps = conexion.prepareStatement("SELECT ID_JUGADOR FROM Jugador WHERE ID_GRUPO = ?")) {
                    ps.setInt(1, idGrupo);
                    try (ResultSet rs2 = ps.executeQuery()) {
                        while (rs2.next()) {
                            //Se obtiene los id de los jugadores
                            miembros.add(rs2.getInt("ID_JUGADOR"));
                        }
                    }
                }
                return new GrupoJuego(
                        rs.getString("nombreGrupo"),
                        rs.getString("descripcion"),
                        miembros
                );
            } catch (SQLException | IOException e) {
                throw new RuntimeException("Error al parsear GrupoJuego", e);
            }
        };
    }

    public Function<ResultSet, Jugador> parseadorJugador() {
        return rs -> {
            try {
                DireccionJuego direccion = null;
                int idDireccion = rs.getInt("ID_DIRECCION");

                //Si no es nulo se sigue
                if (!rs.wasNull() && idDireccion > 0) {
                    try (PreparedStatement ps = conexion.prepareStatement("SELECT * FROM DireccionJuego WHERE ID_DIRECCION = ?")) {
                        ps.setInt(1, idDireccion);
                        try (ResultSet rs2 = ps.executeQuery()) {
                            if (rs2.next()) {
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

                //Igualmente, si no es nulo se sigue
                if (!rs.wasNull() && idDireccion > 0) {
                    try (PreparedStatement ps = conexion.prepareStatement("SELECT * FROM DireccionJuego WHERE ID_DIRECCION = ?")) {
                        ps.setInt(1, idDireccion);
                        try (ResultSet rs2 = ps.executeQuery()) {
                            if (rs2.next()) {
                                direccion = parseadorDireccionJuego().apply(rs2);
                            }
                        }
                    }
                }
                //Lista de aventuras con su id a sacar
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

                //Lista de objetos a sacar
                List<ObjetoInventario> inventario = new ArrayList<>();

                try (PreparedStatement ps = conexion.prepareStatement("SELECT * FROM ObjetoInventario WHERE ID_PERSONAJE = ?")) {
                    ps.setInt(1, idPersonaje);
                    try (ResultSet rs2 = ps.executeQuery()) {
                        while (rs2.next()) {
                            inventario.add(new ObjetoInventario(
                                    rs2.getString("nombre"),
                                    rs2.getDouble("peso"),
                                    rs2.getString("descripcionObjeto")
                            ));
                        }
                    }
                }
                Personaje personaje = new Personaje(
                        idJugador,
                        inventario,
                        rs.getDouble("capacidadCarga"),
                        rs.getString("nombrePersonaje"),
                        rs.getString("descripcion"),
                        rs.getString("historia"),
                        Personaje.Clase.valueOf(rs.getString("clase")),
                        Personaje.Raza.valueOf(rs.getString("raza"))
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
                return new ObjetoInventario(
                        rs.getString("nombre"),
                        rs.getDouble("peso"),
                        rs.getString("descripcionObjeto")
                );
            } catch (SQLException e) {
                throw new RuntimeException("Error al parsear ObjetoInventario", e);
            }
        };
    }
}
