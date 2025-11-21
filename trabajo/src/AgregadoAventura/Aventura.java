package AgregadoAventura;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AventuraAccion.class, name = "AventuraAccion"),
        @JsonSubTypes.Type(value = AventuraMisterio.class, name = "AventuraMisterio"),
        @JsonSubTypes.Type(value = Aventura.class, name = "Aventura")
})
public class Aventura {

    //Dificultad a elegir
    public enum Dificultad {
        FACIL, NORMAL, DIFICIL
    }

    private int ID_AVENTURA; //Id de la aventura
    private String nombreAventura; //Nombre de la aventura
    private int duracionSesionesAprox; //Duracion aproximada de las sesiones
    private Dificultad dificultad; //Dificultad de aventura

    /**
     * Constructor que da valores iniciales a los atributos de clase
     *
     * @param nombreAventura        nombre de aventura
     * @param duracionSesionesAprox duracion aproximada de las seiones
     * @param dificultad            dificultad de aventura
     */
    public Aventura(String nombreAventura, int duracionSesionesAprox, Dificultad dificultad) {
        setNombreAventura(nombreAventura);
        setDuracionSesionesAprox(duracionSesionesAprox);
        this.dificultad = dificultad;
    }

    public Aventura() {
    }

    //Getters y setters

    public int getID_AVENTURA() {
        return ID_AVENTURA;
    }

    public void setID_AVENTURA(int ID_AVENTURA) {
        this.ID_AVENTURA = ID_AVENTURA;
    }

    public String getNombreAventura() {
        return nombreAventura;
    }

    /**
     * Se actualiza el nombre de la aventura
     *
     * @param nombreAventura nombre actualizado
     * @throws IllegalArgumentException en caso de estar vacio
     */
    public void setNombreAventura(String nombreAventura) throws IllegalArgumentException {
        if (nombreAventura.trim().isEmpty())
            throw new IllegalArgumentException("Nombre de aventura vacío");

        this.nombreAventura = nombreAventura;
    }

    public int getDuracionSesionesAprox() {
        return duracionSesionesAprox;
    }

    /**
     * Se actualiza la duracion aproximada de las sesiones
     *
     * @param duracionSesionesAprox duracion aproximada
     * @throws IllegalArgumentException en caso de ser cero o negativa
     */
    public void setDuracionSesionesAprox(int duracionSesionesAprox) throws IllegalArgumentException {
        if (duracionSesionesAprox <= 0)
            throw new IllegalArgumentException("Duración aventura invalida");

        this.duracionSesionesAprox = duracionSesionesAprox;
    }

    public Dificultad getDificultad() {
        return dificultad;
    }

    public void setDificultad(Dificultad dificultad) {
        this.dificultad = dificultad;
    }

    @Override
    public String toString() {
        return "Aventura{" +
                "ID_AVENTURA=" + ID_AVENTURA +
                ", nombreAventura='" + nombreAventura + '\'' +
                ", duracionSesionesAprox=" + duracionSesionesAprox +
                ", dificultad=" + dificultad +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Aventura aventura = (Aventura) o;
        return duracionSesionesAprox == aventura.duracionSesionesAprox && Objects.equals(nombreAventura, aventura.nombreAventura) && dificultad == aventura.dificultad;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombreAventura, duracionSesionesAprox, dificultad);
    }
}
