package AgregadoPersonaje;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

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
        this.descripcion = descripcion;
        this.historia = historia;
        this.clase = clase;
        this.raza = raza;
    }

    public Personaje(List<ObjetoInventario> inventario, double capacidadCarga, String nombrePersonaje, String descripcion, String historia, Clase clase, Raza raza) {
        this.inventario = inventario;
        setCapacidadCarga(capacidadCarga);
        this.nombrePersonaje = nombrePersonaje;
        this.descripcion = descripcion;
        this.historia = historia;
        this.clase = clase;
        this.raza = raza;
    }

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

    public void setCapacidadCarga(double capacidadCarga) throws IllegalArgumentException  {
        if (capacidadCarga < 0) {
            throw new IllegalArgumentException("Error, llevas mas de lo que puedes cargar");
        }
        this.capacidadCarga = capacidadCarga;
    }

    public void revisarInventario() {
        System.out.println(inventario);
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


    public void aniadirObjeto(ObjetoInventario objetoInventario) {
        if (!inventario.contains(objetoInventario)) {
            inventario.add(objetoInventario);
            setCapacidadCarga(capacidadCarga - objetoInventario.getPeso());
        }
    }

    public void ordenarInventarioPorID() {
        inventario.sort((a, b) -> Integer.compare(a.getIdObjeto(), b.getIdObjeto()));
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
                ", capacidadCarga= " + capacidadCarga +
                ", nombrePersonaje= " + nombrePersonaje + '\'' +
                ", descripcion= " + descripcion + '\'' +
                ", historia= " + historia + '\'' +
                ", clase= " + clase +
                ", raza= " + raza +
                '}';
    }
}
