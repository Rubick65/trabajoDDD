package AgregadoJugador;

import AgregadoJugador.Repositorio.RepoJugador;


public class mainPruebasRuben {
    public static void main(String[] args) {

        try {
            // Crear repositorio
            RepoJugador repo = new RepoJugador();

            // Borrar todos los jugadores al inicio
            repo.deleteAll();
            System.out.println("Fichero inicializado vacío.");

            // Crear varias direcciones
            DireccionJuego d1 = new DireccionJuego("Madrid", "Calle Mayor", "3A", "28013");
            DireccionJuego d2 = new DireccionJuego("Barcelona", "Rambla 10", "2B", "08002");
            DireccionJuego d3 = new DireccionJuego("Valencia", "Plaza de la Virgen", "1C", "46001");

            //Crear y guardar jugadores
            Jugador j1 = new Jugador("12345678A", "Carlos", d1);
            Jugador j2 = new Jugador("87654321B", "Lucía", d2);
            DirectorDeJuego dir2 = new DirectorDeJuego("12431234L", "Pesdf", d1);
            Jugador j3 = new Jugador("11223344C", "Ana", d3);
            DirectorDeJuego dir1 = new DirectorDeJuego("12435238B", "Pedro", d2);

            repo.save(j1);
            repo.save(j2);
            repo.save(j3);
            repo.save(dir1);

            System.out.println("Buscamos todos los jugadores por su dirección " + repo.buscarJugadorPorDireccion(d1));

            System.out.println("\nJugadores guardados:");
            repo.findAllToList().forEach(System.out::println);

            // Probar autoincremento al agregar un nuevo jugador
            Jugador j4 = new Jugador("55667788D", "Miguel", d1);
            repo.save(j4);

            System.out.println("\nDespués de guardar un nuevo jugador (autoincremento ID):");
            repo.findAllToList().forEach(System.out::println);

            // Buscar por ID
            int buscarId = j2.getID_JUGADOR();
            System.out.println("\nBuscar jugador con ID " + buscarId + ":");
            repo.findByIdOptional(buscarId).ifPresent(System.out::println);

            // Comprobar existencia
            System.out.println("\nExiste jugador con ID " + j3.getID_JUGADOR() + "? " + repo.existsById(j3.getID_JUGADOR()));

            // Contar jugadores
            System.out.println("\nNúmero total de jugadores: " + repo.count());

            // Eliminar un jugador
            repo.deleteById(j2.getID_JUGADOR());
            System.out.println("\nDespués de eliminar jugador con ID " + j1.getID_JUGADOR() + ":");
            repo.findAllToList().forEach(System.out::println);

            // Intentar guardar jugador duplicado (debería lanzar excepción)
            try {
                repo.save(j2);
            } catch (Exception e) {
                System.out.println("\nIntento de guardar jugador duplicado: " + e.getMessage());
            }

            // Borrar todos
            System.out.println("\nDespués de borrar todos los jugadores:");
            repo.findAllToList().forEach(System.out::println);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}
