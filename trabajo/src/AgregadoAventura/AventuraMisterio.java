package AgregadoAventura;

import java.util.Objects;

public class AventuraMisterio extends Aventura {
    private String enigmaPrincipal; //Enigma principal de la aventura

    /**
     * Constructor que da valores iniciales a los atributos de clase
     * @param nombreAventura nombre de la aventura
     * @param duracionSesionesAprox duracion aproximada de las aventuras
     * @param dificultad dificultad de la aventura
     * @param enigmaPrincipal enigma principal de la aventura
     */
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AventuraMisterio that = (AventuraMisterio) o;
        return Objects.equals(enigmaPrincipal, that.enigmaPrincipal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), enigmaPrincipal);
    }
}
