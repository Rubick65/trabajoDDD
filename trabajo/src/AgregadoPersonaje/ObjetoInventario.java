package AgregadoPersonaje;

import java.util.List;
import java.util.Objects;

public class ObjetoInventario {
    private final int ID_OBJETO; //No sabemos si dejarlo final o no
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ObjetoInventario that = (ObjetoInventario) o;
        return ID_OBJETO == that.ID_OBJETO && Double.compare(peso, that.peso) == 0 && Objects.equals(nombre, that.nombre) && Objects.equals(descripcionObjeto, that.descripcionObjeto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID_OBJETO, nombre, descripcionObjeto, peso);
    }
}
