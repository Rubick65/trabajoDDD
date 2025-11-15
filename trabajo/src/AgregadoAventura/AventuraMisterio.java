package AgregadoAventura;

public class AventuraMisterio extends Aventura {
    private String enigmaPrincipal;

    public AventuraMisterio(String nombreAventura, int duracionSesionesAprox, Dificultad dificultad, String enigmaPrincipal) {
        super(nombreAventura, duracionSesionesAprox, dificultad);
        this.enigmaPrincipal = enigmaPrincipal;
    }

    public AventuraMisterio() {}

    public String getEnigmaPrincipal() {
        return enigmaPrincipal;
    }

    @Override
    public String toString() {
        return super.toString() + " AventuraMisterio{" +
                "enigmaPrincipal='" + enigmaPrincipal + '\'' +
                '}';
    }
}
