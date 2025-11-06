package AgregadoPersonaje;

import java.util.List;

public class ObjetoInventario {
    private final int ID_OBJETO;
    private String nombre,descripcionObjeto;
    private double peso;

    public ObjetoInventario(int ID_OBJETO,String nombre, double peso, String descripcionObjeto) {
        this.ID_OBJETO = ID_OBJETO;
        this.nombre = nombre;
        this.peso = peso;
        this.descripcionObjeto = descripcionObjeto;
    }

    public final int getIdObjeto() {
        return ID_OBJETO;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPeso() {
        return peso;
    }

    @Override
    public String toString() {
        return "ID = " + ID_OBJETO + " nombre = " + nombre + " peso = " + peso;
    }
}
