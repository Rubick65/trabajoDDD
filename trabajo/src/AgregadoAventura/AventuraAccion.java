package AgregadoAventura;

import java.util.Objects;

public class AventuraAccion extends Aventura{
    private int cantidadEnemigos; //Cantidad de enemigos
    private int cantidadUbicaciones; //Cantidad de ubicaciones

    /**
     * Constructor que da valores iniciales a los atributos de clase
     * @param nombreAventura nombre de la aventura
     * @param duracionSesionesAprox duracion aproximada de las sesiones
     * @param dificultad dificultad
     * @param cantidadEnemigos cantidad de enemigos de la aventura
     * @param cantidadUbicaciones cantidad de ubicaciones a explorar de la aventura
     */
    public AventuraAccion(String nombreAventura, int duracionSesionesAprox, Dificultad dificultad, int cantidadEnemigos, int cantidadUbicaciones) {
        super(nombreAventura, duracionSesionesAprox, dificultad);
        setCantidadEnemigos(cantidadEnemigos);
        this.cantidadUbicaciones = cantidadUbicaciones;
    }

    public AventuraAccion() {}

    //Getters y setters

    public int getCantidadEnemigos() {
        return cantidadEnemigos;
    }

    public int getCantidadUbicaciones() {
        return cantidadUbicaciones;
    }

    /**
     * Se actualiza la cantidad de enemigos
     * @param cantidadEnemigos cantidad a actualizar
     * @throws IllegalArgumentException en caso de ser cero o negativo
     */
    public void setCantidadEnemigos(int cantidadEnemigos) throws IllegalArgumentException {
        if (cantidadEnemigos <= 0) {
            throw new IllegalArgumentException("Error, tiene que haber al menos 1 enemigo");
        }
        this.cantidadEnemigos = cantidadEnemigos;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AventuraAccion that = (AventuraAccion) o;
        return cantidadEnemigos == that.cantidadEnemigos && cantidadUbicaciones == that.cantidadUbicaciones;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cantidadEnemigos, cantidadUbicaciones);
    }

    @Override
    public String toString() {
        return super.toString() + " AventuraAccion{" +
                "cantidadEnemigos=" + cantidadEnemigos +
                ", cantidadUbicaciones=" + cantidadUbicaciones +
                '}';
    }
}
