package AgregadoPersonaje;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;


public class Personaje {

    //Posibles clases a elegir
    public enum Clase {
        MAGO, GUERRERO, PALADIN, PICARO, DRUIDA, BARDO, CLERIGO, RANGER
    }

    //Posibles razas a elegir
    public enum Raza {
        HUMANO, ORCO, ELFO, ENANO
    }


    private int ID_JUGADOR; //Id de jugador
    private int ID_PERSONAJE = 0; //Id del personaje
    private List<ObjetoInventario> inventario; //Inventario de objetos
    private double capacidadCarga; //Capacidad de carga de objetos
    private String nombrePersonaje, descripcion, historia; //Nombre,descripcion e historia del personaje
    private Clase clase; //Clase del personaje
    private Raza raza; //Raza del personaje

    /**
     * Constructor que da valores iniciales a los atributos de clase
     *
     * @param ID_JUGADOR      id del jugador
     * @param inventario      inventario de objetos
     * @param capacidadCarga  capacidad de carga de objetos del personaje
     * @param nombrePersonaje nombre del personaje
     * @param descripcion     descripcion del personaje
     * @param historia        historia de trasfondo del personaje
     * @param clase           clase del personaje
     * @param raza            raza del personaje
     */
    public Personaje(int ID_JUGADOR, List<ObjetoInventario> inventario, double capacidadCarga, String nombrePersonaje, String descripcion, String historia, Clase clase, Raza raza) {
        this.ID_JUGADOR = ID_JUGADOR;
        this.inventario = inventario;
        setCapacidadCarga(capacidadCarga);
        this.nombrePersonaje = nombrePersonaje;
        setDescripcion(descripcion);
        setHistoria(historia);
        this.clase = clase;
        this.raza = raza;
    }

    public Personaje(List<ObjetoInventario> inventario, double capacidadCarga, String nombrePersonaje, String descripcion, String historia, Clase clase, Raza raza) {
        this.inventario = inventario;
        setCapacidadCarga(capacidadCarga);
        this.nombrePersonaje = nombrePersonaje;
        setDescripcion(descripcion);
        setHistoria(historia);
        this.clase = clase;
        this.raza = raza;
    }


    //Getters y setters

    public double getCapacidadCarga() {
        return capacidadCarga;
    }

    public String getNombrePersonaje() {
        return nombrePersonaje;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getHistoria() {
        return historia;
    }

    public Clase getClase() {
        return clase;
    }

    public Raza getRaza() {
        return raza;
    }

    public List<ObjetoInventario> getInventario() {
        return inventario;
    }

    public void setInventario(List<ObjetoInventario> inventario) {
        this.inventario = inventario;
    }

    /**
     * Se modifica la capacidad de carga
     *
     * @param capacidadCarga capacidad actualizada
     * @throws IllegalArgumentException si es carga negativa da error
     */
    public void setCapacidadCarga(double capacidadCarga) throws IllegalArgumentException {
        if (capacidadCarga < 0)
            throw new IllegalArgumentException("Error, llevas mas de lo que puedes cargar");

        this.capacidadCarga = capacidadCarga;
    }

    /**
     * Se modifica la descripcion
     *
     * @param descripcion a actualizar
     * @throws IllegalArgumentException en caso de estar vacia
     */
    public void setDescripcion(String descripcion) throws IllegalArgumentException {
        if (descripcion.isEmpty())
            throw new IllegalArgumentException("Descripcion no puede estar vacio");

        this.descripcion = descripcion;
    }

    /**
     * Se modifica la historia
     *
     * @param historia a actualizar
     * @throws IllegalArgumentException no puede estar vacia
     */
    public void setHistoria(String historia) throws IllegalArgumentException {
        if (historia.isEmpty())
            throw new IllegalArgumentException("Historia no puede estar vacío");

        this.historia = historia;
    }

    public void setID_PERSONAJE(int ID_PERSONAJE) {
        this.ID_PERSONAJE = ID_PERSONAJE;
    }

    public int getID_PERSONAJE() {
        return ID_PERSONAJE;
    }

    public int getID_JUGADOR() {
        return ID_JUGADOR;
    }

    public void setID_JUGADOR(int ID_JUGADOR) {
        this.ID_JUGADOR = ID_JUGADOR;
    }

    public String revisarInventario() {
        return inventario.toString();
    }

    /**
     * Se tira un objeto del inventario en caso de tenerlo, se actualiza la capacidad de carga
     *
     * @param objeto objeto a tirar
     */
    public void tirarObjeto(ObjetoInventario objeto) {
        if (inventario.contains(objeto)) {
            setCapacidadCarga(capacidadCarga + inventario.get(inventario.indexOf(objeto)).getPeso());
            inventario.remove(objeto);
        } else {
            System.out.println("Objeto no encontrado");
        }
    }

    private boolean pesoLimite(ObjetoInventario objetoInventario) {
        return ((objetoInventario.getPeso() > this.getCapacidadCarga() / 2) &&
                (objetoInventario.getPeso() < this.getCapacidadCarga())
        );
    }

    /**
     * Se agrega un objeto al inventario en caso de no tenerlo, se actualiza la capacidad de carga
     *
     * @param objetoInventario
     */
    public void agregarObjeto(ObjetoInventario objetoInventario) {
        if (pesoLimite(objetoInventario)) {
            setCapacidadCarga(0);
            inventario.add(objetoInventario);
        } else {
            setCapacidadCarga(capacidadCarga - objetoInventario.getPeso());
            inventario.add(objetoInventario);
        }
    }

    /**
     * Se ordena el inventario alfabeticamente
     */
    public void ordenarInventarioPorNombre() {
        inventario.sort(Comparator.comparing(ObjetoInventario::getNombre));
    }

    /**
     * Se ordena el inventario por peso
     */
    public void ordenarInventarioPorPeso() {
        inventario.sort(Comparator.comparingDouble(a -> a.getPeso()));
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Personaje personaje = (Personaje) o;
        return Objects.equals(nombrePersonaje, personaje.nombrePersonaje)
                && Objects.equals(descripcion, personaje.descripcion)
                && Objects.equals(historia, personaje.historia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombrePersonaje, descripcion, historia);
    }

    @Override
    public String toString() {
        return "Personaje{" +
                "ID_JUGADOR=" + ID_JUGADOR +
                ", ID_PERSONAJE=" + ID_PERSONAJE +
                ", inventario=" + inventario +
                ", capacidadCarga=" + capacidadCarga +
                ", nombrePersonaje='" + nombrePersonaje + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", historia='" + historia + '\'' +
                ", clase=" + clase +
                ", raza=" + raza +
                '}';
    }
}
