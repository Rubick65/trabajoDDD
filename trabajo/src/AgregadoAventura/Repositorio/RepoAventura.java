package AgregadoAventura.Repositorio;

import AgregadoAventura.Aventura;
import AgregadoAventura.AventuraAccion;
import AgregadoAventura.AventuraMisterio;
import GestorBaseDeDatos.GestorDB;
import GestorBaseDeDatos.GestorDeParseadores;
import Interfaces.IRepositorioExtend;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.function.Function;

public class RepoAventura implements IRepositorioExtend<Aventura, Integer> {

    private final GestorDB gestorAventura;
    private final String nombreId = "ID_AVENTURA";
    private final String tabla = "Aventura";

    /**
     * Se cargan los datos al crear el repositorio
     * @throws IOException si hay un error al cargar
     */
    public RepoAventura() throws IOException {
        this.gestorAventura = new GestorDB(tabla);
    }

    /**
     * Se buscan las aventuras por su dificultad
     * @param dificultad dificultad a buscar
     * @return lista filtrada con todas esas dificultades
     */
    public List<Aventura> buscarAventuraPorDificultad(Aventura.Dificultad dificultad, Function <ResultSet,Aventura> parsearAventura) throws IOException, SQLException {
       return findAllToList().stream().filter(aventura -> aventura.getDificultad().equals(dificultad)).toList();
    }

    /**
     * Se busca una aventura por id usando optional
     * @param id a buscar
     * @return la aventura con ese id
     */
    @Override
    public Optional<Aventura> findByIdOptional(Integer id) throws IOException {
        return Optional.ofNullable(gestorAventura.findById(id, nombreId, GestorDeParseadores.parseadorAventura(this.gestorAventura.crearConexion())));
    }

    /**
     * Se muestran todas las aventuras
     * @return todas las aventuras
     */
    @Override
    public List<Aventura> findAllToList() throws IOException {
        return gestorAventura.findAllToList(GestorDeParseadores.parseadorAventura(gestorAventura.crearConexion()));
    }

    /**
     * Se cuentan todas las aventuras
     * @return cantidad
     * @throws IOException si esta vacio
     */
    @Override
    public long count() throws IOException {
        return gestorAventura.count();
    }

    /**
     * Se elimina una aventura mediante su id
     * @param id de la aventura a eliminar
     * @throws IOException si no existe.
     */
    @Override
    public void deleteById(Integer id) throws IOException {
        gestorAventura.deleteById(id, nombreId);
    }

    /**
     * Se borran todas las aventuras
     * @throws IOException
     */
    @Override
    public void deleteAll() throws IOException {
        gestorAventura.deleteAll();
    }

    /**
     * Se comprueba si existe una aventura con el id dado
     * @param id a buscar
     * @return si existe o no
     */
    @Override
    public boolean existsById(Integer id) throws IOException {
        return gestorAventura.existById(id, nombreId);
    }

    /**
     * Se busca una aventura mediante su id
     * @param id a buscar
     * @return aventura con ese id
     * @throws IOException si no existe una aventura con ese id
     */
    @Override
    public Aventura findById(Integer id) throws IOException {
        return gestorAventura.findById(id,nombreId,GestorDeParseadores.parseadorAventura(this.gestorAventura.crearConexion()));
    }

    /**
     * Se obtienen todas las aventuras con iterable
     * @return todas las aventuras
     */
    @Override
    public Iterable<Aventura> findAll() throws IOException {
        return gestorAventura.findAllToList(GestorDeParseadores.parseadorAventura(this.gestorAventura.crearConexion()));
    }

    /**
     * Se guardan las aventuras en la lista y se guardan
     * @param entity aventura a guardar
     * @return aventura a guardar
     * @param <S> entidad e hijos
     * @throws Exception en caso de problema al guardar
     */
    @Override
    public <S extends Aventura> S save(S entity) throws Exception {
        Connection conn = null;
        try {
            conn = gestorAventura.crearConexion();
            conn.setAutoCommit(false);

            int idAventura = guardarAventura(entity, conn);

            if (entity instanceof AventuraAccion aA) {
                guardarAventuraAccion(aA,idAventura,conn);
            }
            else if (entity instanceof AventuraMisterio aM) {
                guardarAventuraMisterio(aM,idAventura,conn);
            }

            conn.commit();
            return entity;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback "+ ex.getMessage());
            }
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }

    private int guardarAventura(Aventura aventura, Connection conn) throws SQLException {
        String sql = """
                INSERT INTO aventura (nombreAventura,duracionSesionesAprox,dificultad)
                VALUES (?,?,?)
                ON DUPLICATE KEY UPDATE
                nombreAventura = VALUES(nombreAventura)
                dificultad = VALUES(dificultad)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, aventura.getNombreAventura());
            ps.setInt(2, aventura.getDuracionSesionesAprox());
            ps.setString(3, aventura.getDificultad().toString());
            ps.executeUpdate();
        }

        // Select para sacar el ID de la aventura recién guardado
        String select = """
                    SELECT %s
                    FROM %s
                    WHERE nombreAventura = ?
                """.formatted(nombreId, this.tabla); //AYUDAAAAAAAAAAAAAAAAAAA

        // Preparamos el select
        PreparedStatement ps = conn.prepareStatement(select);
        ps.setString(1, aventura.getNombreAventura());
        return gestorAventura.sacarId(ps);
    }

    private void guardarAventuraAccion(AventuraAccion aA, int idAventura,Connection conn) throws SQLException {
        String sql = """
                INSERT INTO AventuraAccion (ID_AVENTURA,cantidadEnemigos,cantidadUbicaciones)
                VALUES (?,?,?)
                ON DUPLICATE KEY UPDATE
                ID_AVENTURA =  VALUES(ID_AVENTURA)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,idAventura);
            ps.setInt(2,aA.getCantidadEnemigos());
            ps.setInt(3,aA.getCantidadUbicaciones());
            ps.executeUpdate();
        }
    }

    private void guardarAventuraMisterio(AventuraMisterio aM, int idAventura,Connection conn) throws SQLException {
        String sql = """
                INSERT INTO AventuraMisterio (ID_AVENTURA,enigmaPrincipal)
                VALUES (?,?)
                ON DUPLICATE KEY UPDATE
                ID_AVENTURA =  VALUES(ID_AVENTURA)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,idAventura);
            ps.setString(2,aM.getEnigmaPrincipal());
            ps.executeUpdate();
        }
    }


}

