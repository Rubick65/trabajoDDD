package AgregadoPersonaje;

import java.util.ArrayList;

public class mainPruebasPersonaje {
    public static void main(String[] args) {

        Personaje p1 = new Personaje(new ArrayList<ObjetoInventario>(),65.0,"Paco","Feo","Tio fuerte le dejo la novia",Clase.MAGO,Raza.HUMANO);
        Personaje p2 = new Personaje(new ArrayList<ObjetoInventario>(),65.0,"Hernando","Mas feo","Tio fuerte suspendio interfaces",Clase.BARDO,Raza.ORCO);

        ObjetoInventario jarron = new ObjetoInventario(1,"jarron",20.00,"Mu bonico");
        ObjetoInventario amoniaco = new ObjetoInventario(2,"amoniaco",10,"Muy rico");

        //Se busca la descripcion del p1, se añade un objeto y se ve el inventario
        System.out.println(p1.getDescripcion());
        p1.agregarObjeto(jarron);
        p1.revisarInventario();

        System.out.println();

        System.out.println(p1.getCapacidadCarga());
        p1.tirarObjeto(jarron);
        System.out.println(p1.getCapacidadCarga());

        System.out.println();

        p1.revisarInventario();
        p1.agregarObjeto(jarron);
        p1.agregarObjeto(amoniaco);
        p1.revisarInventario();

        p1.ordenarInventarioPorNombre();
        p1.revisarInventario();

        p1.ordenarInventarioPorPeso();
        p1.revisarInventario();
    }
}
