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

    public GrupoJuego(String nombreGrupo, String descripcion, List<Integer> listaMiembros) throws IOException {
        setNombreGrupo(nombreGrupo);
        setDescripcion(descripcion);
        inicializarMiembros(listaMiembros, new RepoJugador());
    }

    protected GrupoJuego() {
    }

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


    public void inicializarMiembros(List<Integer> listaMiembros, RepoJugador repoJugador) throws IOException {
        if (listaMiembros.isEmpty())
            throw new IllegalArgumentException("La lista de jugadores debe tener por lo menos un jugador");

        this.listaMiembros.clear();
        for (Integer id : listaMiembros) {
            agregarJugador(id, repoJugador);
        }
    }

    public int getID_GRUPO() {
        return ID_GRUPO;
    }

    public void setID_GRUPO(int ID_GRUPO) {
        this.ID_GRUPO = ID_GRUPO;
    }

    public void agregarJugador(int idJugador, RepoJugador repoJugador) {
        comprobareExistenciaJugador(idJugador, repoJugador);
        listaMiembros.add(idJugador);
    }

    private static void comprobareExistenciaJugador(int idJugador, RepoJugador repoJugador) {
        if (!repoJugador.existsById(idJugador))
            throw new IllegalArgumentException("No existe ningún jugador con id: " +  idJugador);
    }

    public boolean eliminarJugador(int idJugador) {
        if (listaMiembros.size() == 1)
            throw new IllegalArgumentException("No puedes eliminar todos los jugadores del grupo " + this.getNombreGrupo() + " con ID " + this.getID_GRUPO() + ", primero elimina el grupo");

        return listaMiembros.removeIf(id -> id == idJugador);
    }


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
