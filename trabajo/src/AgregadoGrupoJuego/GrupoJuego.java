package AgregadoGrupoJuego;

import AgregadoJugador.Repositorio.RepoJugador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GrupoJuego {
    private int ID_GRUPO;
    private String nombreGrupo, descripcion;
    private List<Integer> listaMiembros = new ArrayList<>();

    public GrupoJuego(String nombreGrupo, String descripcion, List<Integer> listaMiembros) {
        setNombreGrupo(nombreGrupo);
        setDescripcion(descripcion);
        setListaMiembros(listaMiembros);
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

    public void setListaMiembros(List<Integer> listaMiembros) {
        if (listaMiembros == null || listaMiembros.isEmpty())
            throw new IllegalArgumentException("La lista no puede estar vacía el grupo tiene que tener por lo menos un jugador.");

        this.listaMiembros = new ArrayList<>(listaMiembros);
    }

    public int getID_GRUPO() {
        return ID_GRUPO;
    }

    public void setID_GRUPO(int ID_GRUPO) {
        this.ID_GRUPO = ID_GRUPO;
    }

    public void agregarJugador(int idJugador, RepoJugador repoJugador) throws IOException {
        comprobareExistenciaJugador(idJugador, repoJugador);

        listaMiembros.add(idJugador);
    }

    private static void comprobareExistenciaJugador(int idJugador, RepoJugador repoJugador) throws IOException {
        if (!repoJugador.existsById(idJugador))
            throw new IllegalArgumentException("No existe ningún jugador con el id introducido");
    }

    public boolean eliminarJugador(int idJugador) {
        if (listaMiembros.size() == 1)
            throw new IllegalArgumentException("No puedes eliminar todos los jugadores del grupo " + this.getNombreGrupo() + " con ID " + this.getID_GRUPO() + ", primero elimina el grupo");

        if (listaMiembros.contains(idJugador)) {
            listaMiembros = listaMiembros.stream().filter(id -> idJugador == id).toList();
            return true;
        }
        return false;
    }

//    private void comprobarSiContieneJugador(int idJugador) {
//        if (!listaMiembros.contains(idJugador))
//            throw new IllegalArgumentException("El jugador introducido no se encuentra en la lista");
//    }

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
