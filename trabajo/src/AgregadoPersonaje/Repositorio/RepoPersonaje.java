package AgregadoPersonaje.Repositorio;

import AgregadoPersonaje.Personaje;
import GestorBaseDeDatos.GestorDB;
import GestorBaseDeDatos.GestorDeParseadores;
import Interfaces.IRepositorioExtend;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class RepoPersonaje implements IRepositorioExtend<Personaje, Integer> {


    private GestorDB gestorPersonajes;
    private final String nombreID = "ID_PERSONAJE";

    /**
     * Se cargan los datos del json
     *
     * @throws IOException si ocurre un error al cargar
     */
    public RepoPersonaje() throws IOException {
        this.gestorPersonajes = new GestorDB("Personaje");
    }

    /**
     * Se buscan los personajes por su clase
     *
     * @param clase a buscar
     * @return lista filtrada por clases
     */
    public List<Personaje> buscarPersonajesPorClases(Personaje.Clase clase) throws IOException {
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
     * Se guardan los datos en el json
     *
     * @param entity personaje a actualizar
     * @param <S>    entidad e hijos
     * @return devuelve el personaje actualizado
     * @throws Exception en caso de problemas al actualizar
     */
    @Override
    public <S extends Personaje> S save(S entity) throws Exception {
        return null;
    }

    /**
     * Actualiza la entidad si ya existía antes
     *
     * @param entity Entidad a actualizar
     * @param <S>    Entidad he hijos
     * @return Devuelve la entidad actualizada
     * @throws IOException Lanza excepción en caso de problemas a la hora de la escritura
     */
    public <S extends Personaje> S actualizarDatos(S entity) throws IOException {
        return null;
    }

}

