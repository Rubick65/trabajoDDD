package AgregadoAventura;


import AgregadoAventura.Repositorio.RepoAventura;

import static AgregadoAventura.Aventura.Dificultad;

public class mainPruebasAventura {
    public static void main(String[] args) {

        try {
            // Crear repositorio
            RepoAventura repo = new RepoAventura();

            // Borrar todos los jugadores al inicio
            repo.deleteAll();
            System.out.println("Fichero inicializado vacío.");

            Aventura a1 = new Aventura("El comeCocos",3, Dificultad.FACIL);
            Aventura a2 = new Aventura("El lameVacas",1, Dificultad.NORMAL);
            Aventura a3 = new Aventura("El PateaPerros",5, Dificultad.DIFICIL);


            repo.save(a1);
            repo.save(a2);
            repo.save(a3);

            System.out.println("\nAventuras guardadas:");
            repo.findAllToList().forEach(System.out::println);

            // Probar autoincremento al agregar una nueva aventura
            Aventura a4 = new Aventura("INAZUMA ELEVEN VICTORY ROAD",5, Dificultad.DIFICIL);
            repo.save(a4);

            System.out.println();
            System.out.println("Aventuras dificiles:");
//            repo.buscarAventuraPorDificultad(Dificultad.DIFICIL).forEach(System.out::println);

            System.out.println("\nDespués de guardar una nueva aventura (autoincremento ID):");
            repo.findAllToList().forEach(System.out::println);

            // Buscar por ID
            int buscarId = a2.getID_AVENTURA();
            System.out.println("\nBuscar jugador con ID " + buscarId + ":");
            repo.findByIdOptional(buscarId).ifPresent(System.out::println);

            // Comprobar existencia
            System.out.println("\nExiste jugador con ID " + a3.getID_AVENTURA() + "? " + repo.existsById(a3.getID_AVENTURA()));

            // Contar aventuras
            System.out.println("\nNúmero total de aventuras: " + repo.count());

            // Eliminar una aventura
            repo.deleteById(a2.getID_AVENTURA());
            System.out.println("\nDespués de eliminar jugador con ID " + a1.getID_AVENTURA() + ":");
            repo.findAllToList().forEach(System.out::println);

            // Intentar guardar aventura duplucada (debería lanzar excepción)
            try {
                repo.save(a2);
            } catch (Exception e) {
                System.out.println("\nIntento de guardar jugador duplicado: " + e.getMessage());
            }

            // Borrar todos
            System.out.println("\nDespués de borrar todas las aventuras:");
            repo.findAllToList().forEach(System.out::println);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}
