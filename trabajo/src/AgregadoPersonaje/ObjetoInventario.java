package AgregadoPersonaje;

import java.util.Objects;

public class ObjetoInventario {

    public enum Categoria {
        MALDITO,MAGICO,NORMAL
    }

    private String nombre, descripcionObjeto; //Nombre y descripcion del objeto
    private double peso; //Peso del objeto
    private Categoria categoria;

    /**
     * Constructor que da valores iniciales a los atributos de clase
     *
     * @param nombre            del objeto
     * @param peso              peso del objeto
     * @param descripcionObjeto descripcion del objeto
     */
    public ObjetoInventario(String nombre, double peso, String descripcionObjeto) {
        this.nombre = nombre;
        this.peso = peso;
        this.descripcionObjeto = descripcionObjeto;
        this.categoria = Categoria.NORMAL;
    }

    public ObjetoInventario(String nombre, double peso, String descripcionObjeto, Categoria categoria) {
        this.nombre = nombre;
        this.peso = peso;
        this.descripcionObjeto = descripcionObjeto;
        this.categoria = categoria;
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

    public Categoria getCategoria() { return categoria; }

    @Override
    public String toString() {
        return "ObjetoInventario{" +
                "nombre='" + nombre + '\'' +
                ", descripcionObjeto='" + descripcionObjeto + '\'' +
                ", peso=" + peso +
                ", Categoria=" + categoria +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ObjetoInventario that = (ObjetoInventario) o;
        return Double.compare(peso, that.peso) == 0 && Objects.equals(nombre, that.nombre) && Objects.equals(descripcionObjeto, that.descripcionObjeto) && categoria == that.categoria;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, descripcionObjeto, peso, categoria);
    }
}




