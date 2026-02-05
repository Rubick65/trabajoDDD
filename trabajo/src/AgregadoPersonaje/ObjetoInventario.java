package AgregadoPersonaje;

import java.util.List;
import java.util.Objects;

public class ObjetoInventario {
    private String nombre, descripcionObjeto; //Nombre y descripcion del objeto
    private double peso; //Peso del objeto
    private boolean maldito;

    /**
     * Constructor que da valores iniciales a los atributos de clase
     * @param nombre del objeto
     * @param peso peso del objeto
     * @param descripcionObjeto descripcion del objeto
     */
    public ObjetoInventario(String nombre, double peso, String descripcionObjeto) {
        this.nombre = nombre;
        this.peso = peso;
        this.descripcionObjeto = descripcionObjeto;
        this.maldito = false;
    }

    public ObjetoInventario(String nombre, double peso, String descripcionObjeto, boolean maldito) {
        this.nombre = nombre;
        this.peso = peso;
        this.descripcionObjeto = descripcionObjeto;
        this.maldito = maldito;
    }

    public ObjetoInventario() {
    }

    //Getters y setters

    public String getNombre() {
        return nombre;
    }

    public double getPeso() {
        return peso;
    }

    public String getDescripcionObjeto() {
        return descripcionObjeto;
    }

    public boolean getMaldito() { return maldito; }

    @Override
    public String toString() {
        return "ObjetoInventario{" +
                "nombre='" + nombre + '\'' +
                ", descripcionObjeto='" + descripcionObjeto + '\'' +
                ", peso=" + peso +
                ", maldito=" + maldito +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ObjetoInventario that = (ObjetoInventario) o;
        return Double.compare(peso, that.peso) == 0 && maldito == that.maldito && Objects.equals(nombre, that.nombre) && Objects.equals(descripcionObjeto, that.descripcionObjeto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, descripcionObjeto, peso, maldito);
    }
}


