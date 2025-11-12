package AgregadoJugador;

import java.util.Objects;

public class DireccionJuego {
    private String ciudad, calle, piso, codigoPostal;

    /**
     * Constructor por defecto para los atributos
     *
     * @param ciudad Ciudad a la que pertenece la dirección
     * @param calle  Calle a la que pertenece la dirección
     * @param piso   Piso a la que pertenece la dirección
     */
    public DireccionJuego(String ciudad, String calle, String piso, String codigoPostal) {
        setCiudad(ciudad);
        setCalle(calle);
        setPiso(piso);
        setCodigoPostal(codigoPostal);
    }

    public DireccionJuego(){

    }

    // Getters y Setters de los atributos
    public String getCiudad() {
        return ciudad;
    }

    private void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCalle() {
        return calle;
    }

    private void setCalle(String calle) {
        this.calle = calle;
    }

    public String getPiso() {
        return piso;
    }

    private void setPiso(String piso) {
        this.piso = piso;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) throws IllegalArgumentException {
        if (codigoPostal.length() != 5)
            throw new IllegalArgumentException("El código postal debe tener 5 dígitos");
        this.codigoPostal = codigoPostal;
    }

    // Equals con todos los atributos de clase
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DireccionJuego that = (DireccionJuego) o;
        return Objects.equals(ciudad, that.ciudad) && Objects.equals(calle, that.calle) && Objects.equals(piso, that.piso) && Objects.equals(codigoPostal, that.codigoPostal);
    }

    // Hash code con todos los atributos de clase
    @Override
    public int hashCode() {
        return Objects.hash(ciudad, calle, piso, codigoPostal);
    }

    @Override
    public String toString() {
        return "Direccion{" +
                "ciudad='" + ciudad + '\'' +
                ", calle='" + calle + '\'' +
                ", piso='" + piso + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                '}';
    }
}
