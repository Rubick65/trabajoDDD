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

    public static Function<ResultSet, Aventura> parseadorAventura(Connection conexion) {
        return rs -> {
            try {
                //Datos generales padres e hijos
                int id = rs.getInt("ID_AVENTURA");
                String nombreAventura = rs.getString("nombreAventura");
                int duracionSesiones = rs.getInt("duracionSesionesAprox");
                Aventura.Dificultad dificultad = Aventura.Dificultad.valueOf(rs.getString("dificultad"));

                //Comprobamos si existe el id en otras tablas para parsear sus hijos en ese caso
                boolean existeEnAccion = Statment.existeEn(conexion, "AventuraAccion", "ID_AVENTURA", id);
                boolean existeEnMisterio = Statment.existeEn(conexion, "AventuraMisterio", "ID_AVENTURA", id);

                //Dependiendo del resultado de los boolean, se generará el tipo adecuado de objeto y se devolverá
                if (existeEnAccion) {
                    String sql = "SELECT cantidadEnemigos,cantidadUbicaciones FROM AventuraAccion WHERE ID_AVENTURA = ?";

                    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                        ps.setInt(1, id);

                        try (ResultSet rs2 = ps.executeQuery()) {
                            if (rs2.next()) {
                                AventuraAccion aventura = new AventuraAccion(nombreAventura, duracionSesiones, dificultad, rs2.getInt("cantidadEnemigos"), rs2.getInt("cantidadUbicaciones"));
                                aventura.setID_AVENTURA(rs.getInt("ID_AVENTURA"));
                                return aventura;
                            }
                        } catch (SQLException ex) {
                            throw new RuntimeException("Error al parsear aventura accion", ex);
                        }
                    }
                }
                if (existeEnMisterio) {
                    String sql = "SELECT enigmaPrincipal FROM AventuraMisterio WHERE ID_AVENTURA = ?";

                    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                        ps.setInt(1, id);

                        try (ResultSet rs2 = ps.executeQuery()) {
                            if (rs2.next()) {
                                AventuraMisterio aventura = new AventuraMisterio(nombreAventura, duracionSesiones, dificultad, rs2.getString("enigmaPrincipal"));
                                aventura.setID_AVENTURA(rs.getInt("ID_AVENTURA"));
                                return aventura;
                            }
                        } catch (SQLException ex) {
                            throw new RuntimeException("Error al parsear aventura Misterio", ex);
                        }
                    }
                }
                //En caso de que los 2 sean false, se llegará aquí.
                Aventura aventura = new Aventura(nombreAventura, duracionSesiones, dificultad);
                aventura.setID_AVENTURA(rs.getInt("ID_AVENTURA"));
                return aventura;
            } catch (SQLException e) {
                throw new RuntimeException("Error al parsear aventura", e);
            }
        };
    }
//
//    public static Function<ResultSet, AventuraAccion> parseadorAventuraAccion(Connection conex) {
//        return rs -> {
//            try {
//                AventuraAccion aventuraAccion = new AventuraAccion(
//                        rs.getString("nombreAventura"),
//                        rs.getInt("duracionSesionesAprox"),
//                        Aventura.Dificultad.valueOf(rs.getString("dificultad")),
//                        rs.getInt("cantidadEnemigos"),
//                        rs.getInt("cantidadUbicaciones")
//                );
//                aventuraAccion.setID_AVENTURA(rs.getInt("ID_AVENTURA"));
//                return aventuraAccion;
//            } catch (SQLException e) {
//                throw new RuntimeException("Error al parsear aventura Accion", e);
//            }
//        };
//    }
//
//    public static Function<ResultSet, AventuraMisterio> parseadorAventuraMisterio(Connection conexion) {
//        return rs -> {
//            try {
//                AventuraMisterio aventuraMisterio = new AventuraMisterio(
//                        rs.getString("nombreAventura"),
//                        rs.getInt("duracionSesionesAprox"),
//                        Aventura.Dificultad.valueOf(rs.getString("dificultad")),
//                        rs.getString("enigmaPrincipal")
//                );
//                aventuraMisterio.setID_AVENTURA(rs.getInt("ID_AVENTURA"));
//                return aventuraMisterio;
//            } catch (SQLException e) {
//                throw new RuntimeException("Error al parsear aventura misterio", e);
//            }
//        };
//    }

    public static Function<ResultSet, DireccionJuego> parseadorDireccionJuego(Connection conexion) {
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

    public static Function<ResultSet, GrupoJuego> parseadorGrupoJuego(Connection conexion) {
        return rs -> {
            try {
                int idGrupo = rs.getInt("ID_GRUPO");
                //Lista de miembros a sacar
                List<Integer> miembros = new ArrayList<>();

                Statment.aniadirAListaEnteros(conexion, miembros, "ID_JUGADOR", "JUGADOR_GRUPOJUEGO", "ID_GRUPOJUEGO", idGrupo);

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

    public static Function<ResultSet, Jugador> parseadorJugador(Connection conexion) {
        return rs -> {
            try {
                //Datos comunes padres e hijos
                DireccionJuego direccion = null;
                int idDireccion = rs.getInt("ID_DIRECCION");
                int idJugador = rs.getInt("ID_JUGADOR");
                String dni = rs.getString("DNI");
                String nombre = rs.getString("NOMBRE");

                boolean direccionEsNula = rs.wasNull();


                //Si no es nulo se sigue
                if (!direccionEsNula && idDireccion > 0)
                    direccion = Statment.obtenerDireccionDeJuego(conexion, idDireccion);

                //Comprobamos si el id esta en director de juego
                boolean existeEnDirectorJuego = Statment.existeEn(conexion, "DirectorDeJuego", "ID_JUGADOR", idJugador);

                //En caso de estarlo, se parseara director de juego
                if (existeEnDirectorJuego) {
                    //Lista de aventuras con su id a sacar
                    List<Integer> listaAventuras = new ArrayList<>();
                    Statment.aniadirAListaEnteros(conexion, listaAventuras, "ID_AVENTURA", "DirectorAventura", "ID_DIRECTOR", idJugador);
                    DirectorDeJuego d = new DirectorDeJuego(dni, nombre, direccion, listaAventuras);
                    d.setID_JUGADOR(idJugador);
                    return d;
                }

                //En caso de no ser director de juego, se devuelve como jugador
                Jugador jugador = new Jugador(dni, nombre, direccion);
                jugador.setID_JUGADOR(idJugador);
                return jugador;
            } catch (SQLException e) {
                throw new RuntimeException("Error al parsear Jugador ", e);
            }
        };
    }

    public static Function<ResultSet, DirectorDeJuego> parseadorDirectorDeJuego(Connection conexion) {
        return rs -> {
            try {
                DireccionJuego direccion = null;
                int idDireccion = rs.getInt("ID_DIRECCION");

                //Igualmente, si no es nulo se sigue
                if (!rs.wasNull() && idDireccion > 0) {
                    direccion = Statment.obtenerDireccionDeJuego(conexion, idDireccion);
                }

                //Lista de aventuras con su id a sacar
                List<Integer> listaAventuras = new ArrayList<>();
                int idDirector = rs.getInt("ID_JUGADOR");

                Statment.aniadirAListaEnteros(conexion, listaAventuras, "ID_AVENTURA", "DirectorAventura", "ID_DIRECTOR", idDirector);

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

    public static Function<ResultSet, Personaje> parseadorPersonaje(Connection conexion) {
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
                                    rs2.getString("descripcionObjeto"),
                                    ObjetoInventario.Categoria.valueOf(rs2.getString("categoria"))
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

    public static Function<ResultSet, ObjetoInventario> parseadorObjetoInventario(Connection conexion) {
        return rs -> {
            try {
                return new ObjetoInventario(
                        rs.getString("nombre"),
                        rs.getDouble("peso"),
                        rs.getString("descripcionObjeto"),
                        ObjetoInventario.Categoria.valueOf(rs.getString("categoria"))
                );
            } catch (SQLException e) {
                throw new RuntimeException("Error al parsear ObjetoInventario", e);
            }
        };
    }
}
