package AgregadoAventura;

public class AventuraMisterio extends Aventura {
    private String enigmaPrincipal;

    public AventuraMisterio(String nombreAventura, int duracionSesionesAprox, Dificultad dificultad, String enigmaPrincipal) throws Exception {
        super(nombreAventura, duracionSesionesAprox, dificultad);
        this.enigmaPrincipal = enigmaPrincipal;
    }

    public String getEnigmaPrincipal() {
        return enigmaPrincipal;
    }

    @Override
    public String toString() {
        return "AventuraMisterio{" +
                "enigmaPrincipal='" + enigmaPrincipal + '\'' +
                '}';
    }
}
