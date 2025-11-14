package AgregadoPersonaje;

import java.util.List;
import java.util.Objects;

public class ObjetoInventario {
    private String nombre, descripcionObjeto;
    private double peso;

    public ObjetoInventario(String nombre, double peso, String descripcionObjeto) {
        this.nombre = nombre;
        this.peso = peso;
        this.descripcionObjeto = descripcionObjeto;
    }

    public ObjetoInventario() {
    }

    public String getNombre() {
        return nombre;
    }

    public double getPeso() {
        return peso;
    }

    @Override
    public String toString() {
        return "ObjetoInventario{" +
                "nombre='" + nombre + '\'' +
                ", descripcionObjeto='" + descripcionObjeto + '\'' +
                ", peso=" + peso +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ObjetoInventario that = (ObjetoInventario) o;
        return Double.compare(peso, that.peso) == 0 && Objects.equals(nombre, that.nombre) && Objects.equals(descripcionObjeto, that.descripcionObjeto);
    }
    @Override
    public int hashCode() {
        return Objects.hash(nombre, descripcionObjeto, peso);
    }
}


