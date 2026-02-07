package Servicio;

import AgregadoPersonaje.ObjetoInventario;
import AgregadoPersonaje.Personaje;

import java.util.List;

public class GestorInventario {

    /**
     * Se agrega un objeto al inventario en caso de no tenerlo, se actualiza la capacidad de carga
     *
     * @param objetoInventario
     */
    public void agregarObjeto(Personaje personaje, ObjetoInventario objetoInventario) {

        comprobarValidezObjeto(personaje, objetoInventario);
        personaje.agregarObjeto(objetoInventario);
    }

    private void comprobarValidezObjeto(Personaje personaje, ObjetoInventario objetoInventario) {
        double pesoObjeto = pesoSegunRaza(personaje, objetoInventario);

        if (pesoObjeto > personaje.getCapacidadCarga())
            throw new IllegalArgumentException("El objeto pesa demasiado para guardarlo en el inventario ahora mismo");

        if (pesoLimite(personaje, pesoObjeto))
            personaje.restarCapacidadDeCarga(personaje.getCapacidadCarga());
        else
            personaje.restarCapacidadDeCarga(pesoObjeto);

        if (objetoInventario.getCategoria() == ObjetoInventario.Categoria.MALDITO)
            efectoMaldito(personaje.getInventario(), objetoInventario);

    }

    private double pesoSegunRaza(Personaje personaje, ObjetoInventario objetoInventario) {
        double peso = objetoInventario.getPeso();
        switch (personaje.getRaza()) {
            case Personaje.Raza.ENANO:
                peso *= 0.98;
                break;
            case Personaje.Raza.ELFO:
                if (objetoInventario.getCategoria() == ObjetoInventario.Categoria.MAGICO)
                    peso *= 0.8;
                break;
            case Personaje.Raza.ORCO:
                if (objetoInventario.getCategoria() == ObjetoInventario.Categoria.NORMAL)
                    peso *= 0.5;
                break;
            default:
                break;
        }

        return peso;
    }

    private boolean pesoLimite(Personaje personaje, double peso) {
        return ((peso > personaje.getCapacidadCarga() / 2) &&
                (peso < personaje.getCapacidadCarga())
        );
    }

    private void efectoMaldito(List<ObjetoInventario> inventario, ObjetoInventario objetoInventario) {
        if (!inventario.isEmpty() && !inventario.contains(objetoInventario))
            inventario.remove((int) (Math.random() * inventario.size()));

    }

    /**
     * Se tira un objeto del inventario en caso de tenerlo, se actualiza la capacidad de carga
     *
     * @param objeto objeto a tirar
     */
    public void tirarObjeto(Personaje personaje, ObjetoInventario objeto) {

        if (personaje.getInventario().contains(objeto)) {
            personaje.setCapacidadCarga(personaje.getCapacidadCarga() + personaje.getInventario().get(personaje.getInventario().indexOf(objeto)).getPeso());
            personaje.getInventario().remove(objeto);
        } else
            throw new IllegalArgumentException("El objeto que intentas tirar no se encuentran en el inventario");

    }
}
