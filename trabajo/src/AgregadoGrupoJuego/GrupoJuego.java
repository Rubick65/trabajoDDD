package AgregadoGrupoJuego;

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

    public void agregarJugador(int idJugador) {
        /*
         Se debe comprobar que el ID del jugador pertenezca realmente a un jugador por ejemplo llamando a una función
         del gestor que compruebe si el ID existe o no, en caso de no existir lanzamos una excepción
         */

        listaMiembros.add(idJugador);
    }

    public void eliminarJugador(int idJugador) {
        comprobarSiJugadorExiste(idJugador);

        listaMiembros.remove(idJugador);
    }

    private void comprobarSiJugadorExiste(int idJugador) {
        if (!listaMiembros.contains(idJugador))
            throw new IllegalArgumentException("El jugador introducido no se encuentra en la lista");
    }

    public void sustituirJugador(int idNuevoJugador, int idJugadorViejo) {
        // Habría que comprobar aquí si el jugador nuevo existe
        comprobarSiJugadorExiste(idJugadorViejo);
        eliminarJugador(idJugadorViejo);
        agregarJugador(idNuevoJugador);
    }
}
