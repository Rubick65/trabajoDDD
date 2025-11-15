package AgregadoPersonaje;

import AgregadoPersonaje.Repositorio.RepoPersonaje;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class cargarPersonajes {
    public static void main(String[] args) throws Exception {
        RepoPersonaje repo = new RepoPersonaje();
        repo.deleteAll();

        // Inventarios de ejemplo
        List<ObjetoInventario> inv1 = new ArrayList<>();
        inv1.add(new ObjetoInventario("Espada corta", 3.5, "Espada de acero básica"));
        inv1.add(new ObjetoInventario("Poción de vida", 0.5, "Restaura 50 puntos de vida"));

        List<ObjetoInventario> inv2 = new ArrayList<>();
        inv2.add(new ObjetoInventario("Arco largo", 2.0, "Arco de madera reforzada"));
        inv2.add(new ObjetoInventario("Flechas", 1.0, "Carcaj con 20 flechas"));

        List<ObjetoInventario> inv3 = new ArrayList<>();
        inv3.add(new ObjetoInventario("Bastón mágico", 4.0, "Bastón de poder arcano"));
        inv3.add(new ObjetoInventario("Libro de hechizos", 1.5, "Libro antiguo de magia"));

        // Personajes
        Personaje p1 = new Personaje(1, inv1, 20, "Thorin", "Guerrero valiente", "Sobrevivió a mil batallas", Personaje.Clase.GUERRERO, Raza.ENANO);
        Personaje p2 = new Personaje(2, inv2, 15, "Legolas", "Arquero experto", "Protector del bosque", Personaje.Clase.RANGER, Raza.ELFO);
        Personaje p3 = new Personaje(3, inv3, 12, "Gandalf", "Mago sabio", "Viajero de tierras lejanas", Personaje.Clase.MAGO, Raza.HUMANO);

        Personaje p4 = new Personaje(new ArrayList<>(), 10, "Aragorn", "Líder de hombres", "Descendiente de reyes", Personaje.Clase.GUERRERO, Raza.HUMANO);
        Personaje p5 = new Personaje(new ArrayList<>(), 8, "Frodo", "Pequeño pero valiente", "Portador del anillo", Personaje.Clase.PICARO, Raza.HUMANO);

        Personaje p6 = new Personaje(new ArrayList<>(), 14, "Elrond", "Sabio y anciano", "Señor de Rivendel", Personaje.Clase.CLERIGO, Raza.ELFO);

        // Guardarlos en el repo
        repo.save(p1);
        repo.save(p2);
        repo.save(p3);
        repo.save(p4);
        repo.save(p5);
        repo.save(p6);
    }
}
