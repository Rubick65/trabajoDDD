package AgregadoAventura;

public class AventuraAccion extends Aventura{
    private int cantidadEnemigos;
    private int cantidadUbicaciones;

    public AventuraAccion(String nombreAventura, int duracionSesionesAprox, Dificultad dificultad, int cantidadEnemigos, int cantidadUbicaciones) {
        super(nombreAventura, duracionSesionesAprox, dificultad);
        setCantidadEnemigos(cantidadEnemigos);
        this.cantidadUbicaciones = cantidadUbicaciones;
    }

    public int getCantidadEnemigos() {
        return cantidadEnemigos;
    }

    public int getCantidadUbicaciones() {
        return cantidadUbicaciones;
    }

    public void setCantidadEnemigos(int cantidadEnemigos) throws IllegalArgumentException {
        if (cantidadEnemigos <= 0) {
            throw new IllegalArgumentException("Error, tiene que haber al menos 1 enemigo");
        }
        this.cantidadEnemigos = cantidadEnemigos;
    }

    @Override
    public String toString() {
        return "AventuraAccion{" +
                "cantidadEnemigos=" + cantidadEnemigos +
                ", cantidadUbicaciones=" + cantidadUbicaciones +
                '}';
    }
}
