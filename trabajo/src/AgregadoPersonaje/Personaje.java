package AgregadoPersonaje;

import java.util.Comparator;
import java.util.List;

enum Raza {
    HUMANO,ORCO,ELFO,ENANO
}

enum Clase {
    MAGO,GUERRERO,PALADIN,PICARO,DRUIDA,BARDO,CLERIGO,RANGER
}

public class Personaje {
    private int ID_JUGADOR;
    private int ID_PERSONAJE = 0;
    private List <ObjetoInventario> inventario;
    private double capacidadCarga;
    private String nombrePersonaje,descripcion,historia;
    private Clase clase;
    private Raza raza;

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

    public Personaje(){}

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

    public void setCapacidadCarga(double capacidadCarga) throws IllegalArgumentException  {
        if (capacidadCarga < 0) {
            throw new IllegalArgumentException("Error, llevas mas de lo que puedes cargar");
        }
        this.capacidadCarga = capacidadCarga;
    }

    public void setDescripcion(String descripcion) throws IllegalArgumentException {
        if (descripcion.isEmpty()) {
            throw new IllegalArgumentException("Descripcion no puede estar vacio");
        }
        this.descripcion = descripcion;
    }

    public void setHistoria(String historia) throws IllegalArgumentException {
        if (historia.isEmpty()) {
            throw new IllegalArgumentException("Historia no puede estar vacio");
        }
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

    public void tirarObjeto(ObjetoInventario objeto) {
        if (inventario.contains(objeto)) {
            setCapacidadCarga(capacidadCarga + inventario.get(inventario.indexOf(objeto)).getPeso());
            inventario.remove(inventario.indexOf(objeto));
        }
        else {
            System.out.println("Objeto no encontrado");
        }
    }

    public void agregarObjeto(ObjetoInventario objetoInventario) {
        if (!inventario.contains(objetoInventario)) {
            inventario.add(objetoInventario);
            setCapacidadCarga(capacidadCarga - objetoInventario.getPeso());
        }
    }

    public void ordenarInventarioPorNombre() {
        inventario.sort((a, b) -> a.getNombre().compareTo(b.getNombre()));
    }

    public void ordenarInventarioPorPeso() {
        inventario.sort(Comparator.comparingDouble(a -> a.getPeso()));
    }

    @Override
    public String toString() {
        return "Personaje{" +
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
