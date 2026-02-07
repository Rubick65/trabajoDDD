package AgregadoPersonaje;


import AgregadoPersonaje.Repositorio.RepoPersonaje;
import Servicio.GestorInventario;

import java.util.ArrayList;

import static AgregadoPersonaje.Personaje.Clase;

public class mainPruebasPersonaje {
    public static void main(String[] args) {

        try {
            // Crear repositorio
            RepoPersonaje repo = new RepoPersonaje();
            GestorInventario gestorInventario = new GestorInventario();

            // Borrar todos los jugadores al inicio
            repo.deleteAll();

            Personaje p1 = new Personaje(new ArrayList<ObjetoInventario>(), 70.5, "Bartolomeo", "Feo a mas no poder", "Se perdio", Clase.CLERIGO, Personaje.Raza.ORCO);
            Personaje p2 = new Personaje(new ArrayList<ObjetoInventario>(), 40.5, "Paca", "algo guapa", "Se perdio mas", Clase.BARDO, Personaje.Raza.ELFO);
            Personaje p3 = new Personaje(new ArrayList<ObjetoInventario>(), 20.5, "El tusi", "Hipster", "Se aburre", Clase.PALADIN, Personaje.Raza.HUMANO);

            ObjetoInventario jabon = new ObjetoInventario("Jabon", 10.0, "Util");

            gestorInventario.agregarObjeto(p1, jabon);
            gestorInventario.agregarObjeto(p2, new ObjetoInventario("Barril", 30.0, "No tan util"));

            p1.getInventario().forEach(System.out::println);

            repo.save(p1);
            repo.save(p2);
            repo.save(p3);

            System.out.println("\nPersonajes guardados:");
            repo.findAllToList().forEach(System.out::println);

            // Probar autoincremento al agregar un nuevo personaje
            Personaje p4 = new Personaje(new ArrayList<ObjetoInventario>(), 99.5, "asasas", "Titanico", "Rey de egipto", Clase.PALADIN, Personaje.Raza.HUMANO);
            repo.save(p4);

            System.out.println();
            System.out.println("Paladines");
            repo.buscarPersonajesPorClases(Clase.PALADIN).forEach(System.out::println);

            System.out.println("\nDespués de guardar un nuevo personaje (autoincremento ID):");
            repo.findAllToList().forEach(System.out::println);

            // Buscar por ID
            int buscarId = p2.getID_PERSONAJE();
            System.out.println("\nBuscar personaje con ID " + buscarId + ":");
            repo.findByIdOptional(buscarId).ifPresent(System.out::println);

            // Comprobar existencia
            System.out.println("\nExiste personaje con ID " + p3.getID_PERSONAJE() + "? " + repo.existsById(p3.getID_PERSONAJE()));

            // Contar personajes
            System.out.println("\nNúmero total de personajes: " + repo.count());

            // Eliminar un personaje
            repo.deleteById(p2.getID_PERSONAJE());
            System.out.println("\nDespués de eliminar jugador con ID " + p1.getID_PERSONAJE() + ":");
            repo.findAllToList().forEach(System.out::println);

            // Intentar guardar personaje duplicado (debería lanzar excepción)
            try {
                repo.save(p2);
            } catch (Exception e) {
                System.out.println("\nIntento de guardar personaje duplicado: " + e.getMessage());
            }

            // Borrar todos
            System.out.println("\nDespués de borrar todas las aventuras:");
            repo.findAllToList().forEach(System.out::println);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}
