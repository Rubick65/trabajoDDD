package AgregadoGrupoJuego;

import AgregadoGrupoJuego.Repositorio.RepoGrupoJuego;
import AgregadoJugador.DireccionJuego;
import AgregadoJugador.Jugador;
import AgregadoJugador.Repositorio.RepoJugador;

import java.io.IOException;
import java.util.Arrays;

public class mainPruebaRubenGrupo {
    public static void main(String[] args) {
        try {
            // Inicializamos repositorios
            RepoJugador repoJugador = new RepoJugador();
            RepoGrupoJuego repoGrupo = new RepoGrupoJuego();

            repoJugador.deleteAll();
            repoGrupo.deleteAll();

            // Crear jugadores de prueba
            DireccionJuego d1 = new DireccionJuego("Madrid", "Calle Mayor", "3A", "28013");
            DireccionJuego d2 = new DireccionJuego("Barcelona", "Rambla 10", "2B", "08002");
            DireccionJuego d3 = new DireccionJuego("Valencia", "Plaza de la Virgen", "1C", "46001");

            //Crear y guardar jugadores
            Jugador j1 = new Jugador("12345678A", "Carlos", d1);
            Jugador j2 = new Jugador("87654321B", "Lucía", d2);
            Jugador j3 = new Jugador("11223344C", "Ana", d3);

            // Guardamos jugadores en el repo
            repoJugador.save(j1);
            repoJugador.save(j2);
            repoJugador.save(j3);

            System.out.println("Jugadores guardados:");
            repoJugador.findAllToList().forEach(System.out::println);

            // Crear un grupo de juego
            GrupoJuego grupo = new GrupoJuego("Heroes", "Grupo de jugadores de prueba",
                    Arrays.asList(j1.getID_JUGADOR(), j2.getID_JUGADOR()));

            // Guardar grupo en el repo
            repoGrupo.save(grupo);

            System.out.println("\nGrupos guardados:");
            repoGrupo.findAllToList().forEach(System.out::println);

            // Agregar un jugador al grupo
            grupo.agregarJugador(j3.getID_JUGADOR(), repoJugador);
            repoGrupo.save(grupo); // actualizar el grupo en el repo
            System.out.println("\nDespués de agregar a Juan:");
            repoGrupo.findAllToList().forEach(System.out::println);

             //Sustituir jugador
            Jugador j4 = new Jugador("55667788D", "Miguel", d1);
            repoJugador.save(j4);

            repoGrupo.save(grupo);
            System.out.println("\nDespués de sustituir a Lola por Ana:");
            repoGrupo.findAllToList().forEach(System.out::println);

            // Intentar eliminar último jugador (debería lanzar excepción)
            try {
                grupo.eliminarJugador(j1.getID_JUGADOR());
                grupo.eliminarJugador(j2.getID_JUGADOR());
                grupo.eliminarJugador(j4.getID_JUGADOR());
            } catch (IllegalArgumentException e) {
                System.out.println("\nExcepción al intentar eliminar todos los jugadores: " + e.getMessage());
            }

            // Mostrar estado final
            System.out.println("\nEstado final del grupo:");
            repoGrupo.findAllToList().forEach(System.out::println);

            // Eliminar grupo por ID
//            repoGrupo.deleteById(grupo.getID_GRUPO());
//            System.out.println("\nDespués de eliminar el grupo:");
//            repoGrupo.findAllToList().forEach(System.out::println);


        } catch (IOException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }


    }
}
