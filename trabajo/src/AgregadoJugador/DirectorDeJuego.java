package AgregadoJugador;

import AgregadoAventura.Repositorio.RepoAventura;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DirectorDeJuego extends Jugador {

    private List<Integer> listaAventuras = new ArrayList<>();
    private int aventuraSeleccionada;

    /**
     * Constructor que da valores iniciales a los atributos de clase
     *
     * @param DNI            Dni de la persona
     * @param nombre         Nombre de la persona
     * @param direccionJuego Objeto dirección de juego que indica el lugar en donde el jugador va a jugar
     * @param listaAventuras Lista con los ids de la o las aventuras que el director de juego está dirigiendo
     *
     */
    public DirectorDeJuego(String DNI, String nombre, DireccionJuego direccionJuego, List<Integer> listaAventuras) {
        super(DNI, nombre, direccionJuego);
        setListaAventuras(listaAventuras);
    }

    /**
     * Constructor que da valores iniciales a los atributos de clase sin la lista de aventuras
     *
     * @param DNI            Dni de la persona
     * @param nombre         Nombre de la persona
     * @param direccionJuego Objeto dirección de juego que indica el lugar en donde el jugador va a jugar
     *
     */
    public DirectorDeJuego(String DNI, String nombre, DireccionJuego direccionJuego) {
        super(DNI, nombre, direccionJuego);
    }


    // Getters y Setters de los atributos
    public List<Integer> getListaAventuras() {
        return listaAventuras;
    }

    private void setListaAventuras(List<Integer> listaAventuras) {
        this.listaAventuras = listaAventuras;
    }

    public int getAventuraSeleccionada() {
        return aventuraSeleccionada;
    }


    /**
     * Agrega una aventura al director de juego si y solo si la aventura existe
     *
     * @param idAventura   id de la aventura a agregar
     * @param repoAventura Repositorio de aventuras para comprobar
     */
    public void agregarAventura(int idAventura, RepoAventura repoAventura) throws IOException {
        if (!repoAventura.existsById(idAventura))
            throw new IllegalArgumentException("No existe ninguna aventura con ese id");
        listaAventuras.add(idAventura);
    }

    /**
     * Método que pone la aventura que el director de juego dirige en estos momentos
     *
     * @param idAventuraSeleccionada Id de la aventura que se quiere seleccionar
     * @throws Exception Lanza una excepción en caso de que la aventura no se encuentre en la lista de aventuras del director de juego
     */
    public void setAventuraSeleccionada(int idAventuraSeleccionada) throws Exception {
        if (idAventuraSeleccionada != 0)
            comprobarAventura(idAventuraSeleccionada);

        this.aventuraSeleccionada = idAventuraSeleccionada;
    }

    /**
     * Método que elimina una aventura de la lista de aventuras guardadas del director
     *
     * @param idAventura Id de la aventura ha eliminar
     * @throws Exception Lanza una excepción si la aventura a eliminar no existe
     */
    public void eliminarAventura(int idAventura) throws Exception {
        comprobarAventura(idAventura);
        listaAventuras.remove(idAventura);
    }

    /**
     * Método que comprueba si el id de una aventura existe en la lista de aventuras guardadas por el director
     *
     * @param idAventura Id de la aventura que se va a comprobar
     * @throws Exception Lanza una excepción en caso de que la aventura no exista en la lista de aventuras guardadas
     */
    private void comprobarAventura(int idAventura) throws Exception {
        if (!listaAventuras.contains(idAventura)) throw new Exception("El director no posee esta aventura");
    }

    @Override
    public String toString() {
        return super.toString() + "DirectorDeJuego{" +
                "listaAventuras=" + listaAventuras +
                ", aventuraSeleccionada=" + aventuraSeleccionada +
                '}';
    }
}
