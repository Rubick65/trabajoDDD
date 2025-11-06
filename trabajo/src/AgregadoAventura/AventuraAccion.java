package AgregadoAventura;

public class AventuraAccion extends Aventura{
    private int cantidadEnemigosActuales;
    private int cantidadUbicaciones;

    public AventuraAccion(String nombreAventura, int duracionSesionesAprox, Dificultad dificultad, int cantidadEnemigosActuales, int cantidadUbicaciones) throws Exception {
        super(nombreAventura, duracionSesionesAprox, dificultad);
        setCantidadEnemigos(cantidadEnemigosActuales);
        this.cantidadUbicaciones = cantidadUbicaciones;
    }

    public int getCantidadEnemigosActuales() {
        return cantidadEnemigosActuales;
    }

    public int getCantidadUbicaciones() {
        return cantidadUbicaciones;
    }

    public void setCantidadEnemigos(int cantidadEnemigos) throws IllegalArgumentException {
        if (cantidadEnemigos <= 0) {
            throw new IllegalArgumentException("Error, tiene que haber al menos 1 enemigo");
        }
        this.cantidadEnemigosActuales = cantidadEnemigos;
    }

    public void restarEnemigosDerrotados (int derrotados) throws IllegalArgumentException {
        if (derrotados - cantidadEnemigosActuales < 0) {
            throw new IllegalArgumentException("Error, enemigos negativos");
        }
        cantidadEnemigosActuales -= derrotados;
    }

    @Override
    public String toString() {
        return "AventuraAccion{" +
                "cantidadEnemigos=" + cantidadEnemigosActuales +
                ", cantidadUbicaciones=" + cantidadUbicaciones +
                '}';
    }
}
