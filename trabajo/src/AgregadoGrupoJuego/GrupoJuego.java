package AgregadoGrupoJuego;

import AgregadoJugador.Repositorio.RepoJugador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GrupoJuego {
    private int ID_GRUPO;
    private String nombreGrupo, descripcion;
    private List<Integer> listaMiembros = new ArrayList<>();

    /**
     * Constructor que da valores iniciales a los atributos de clase
     *
     * @param nombreGrupo   Nombre del grupo
     * @param descripcion   Descripción del grupo
     * @param listaMiembros Lista de miembros iniciales del grupo
     * @throws IOException lanza la excepción si ocurre algún problema al intentar comprobar la existencia de los jugadores
     */
    public GrupoJuego(String nombreGrupo, String descripcion, List<Integer> listaMiembros) throws IOException {
        setNombreGrupo(nombreGrupo);
        setDescripcion(descripcion);
        inicializarMiembros(listaMiembros);
    }


    // Getters y Setters de los atributos
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public List<Integer> getListaMiembros() {
        return listaMiembros;
    }


    /**
     * Comprueba que los jugadores sean válido y existan
     *
     * @param listaMiembros lista de miembros a comprobar
     * @throws IOException Lanza excepción en caso de problemas en la lectura de los juagadores
     */
    public void inicializarMiembros(List<Integer> listaMiembros) throws IOException {
        if (listaMiembros.isEmpty())
            throw new IllegalArgumentException("La lista de jugadores debe tener por lo menos un jugador");

        this.listaMiembros.clear();
        for (Integer id : listaMiembros) {
            agregarJugador(id);
        }
    }

    public int getID_GRUPO() {
        return ID_GRUPO;
    }

    public void setID_GRUPO(int ID_GRUPO) {
        this.ID_GRUPO = ID_GRUPO;
    }

    /**
     * Comprueba la existencia del jugador que se quiere añadir
     *
     * @param idJugador Id del jugador a añadir
     */
    public void agregarJugador(int idJugador) throws IOException {
        listaMiembros.add(idJugador);
    }

    /**
     * Comprueba la existencia de un jugdor
     *
     * @param idJugador   Id del jugador a comprobar
     * @param repoJugador Repositorio que guarda al jugador
     */
    private static void comprobareExistenciaJugador(int idJugador, RepoJugador repoJugador) throws IOException {
        if (!repoJugador.existsById(idJugador))
            throw new IllegalArgumentException("No existe ningún jugador con id: " + idJugador);
    }

    /**
     * Elimina un jugador de la lista
     *
     * @param idJugador Id del jugador a eliminar
     * @return True si lo pudo eliminar y false en caso contrario
     */
    public boolean eliminarJugador(int idJugador) {
        if (listaMiembros.size() == 1)
            throw new IllegalArgumentException("No puedes eliminar todos los jugadores del grupo " + this.getNombreGrupo() + " con ID " + this.getID_GRUPO() + ", primero elimina el grupo");

        return listaMiembros.removeIf(id -> id == idJugador);
    }


    /**
     * Compara un objeto externo con este a traves de todos sus atributos
     *
     * @param o the reference object with which to compare.
     * @return True en caso de que coincidan los objetos y false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GrupoJuego that = (GrupoJuego) o;
        return ID_GRUPO == that.ID_GRUPO && Objects.equals(nombreGrupo, that.nombreGrupo) && Objects.equals(descripcion, that.descripcion) && Objects.equals(listaMiembros, that.listaMiembros);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID_GRUPO, nombreGrupo, descripcion, listaMiembros);
    }

    @Override
    public String toString() {
        return "GrupoJuego{" +
                "ID_GRUPO=" + ID_GRUPO +
                ", nombreGrupo='" + nombreGrupo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", listaMiembros=" + listaMiembros +
                '}';
    }
}
