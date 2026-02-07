package AgregadoJugador.Repositorio;

import AgregadoJugador.DireccionJuego;
import AgregadoJugador.Jugador;

import java.io.IOException;

public class PruebaJugador {
    public static void main(String[] args) {
        try {
            RepoJugador repoJugador = new RepoJugador();
            System.out.println(repoJugador.count());

            DireccionJuego direccionJuego = new DireccionJuego("Luna", "Jose manual", "1C", "29024");

            Jugador jugador1 = new Jugador("02790162D", "Pedro", direccionJuego);

//            DirectorDeJuego jugador = new DirectorDeJuego("02790162D", "Jose", direccionJuego);

            repoJugador.save(jugador1);
            repoJugador.findAllToList().forEach(System.out::println);


//            Jugador personaje = repoJugador.findById(18);
//
//            System.out.println(personaje);
//
//            Iterable<Jugador> personajes = repoJugador.findAll();
//
//            personajes.forEach(System.out::println);
//
//
//            repoJugador.deleteAll();
//            System.out.println(repoJugador.count());


            //            Jugador jugador1 = repoJugador.findById(20);

//            System.out.println(jugador1);

//            Optional<Jugador> personaje = repoJugador.findByIdOptional(21);
//
//            personaje.ifPresentOrElse(System.out::println, () -> System.out.println("No existe el personaje"));

//            List<Jugador> personajes = repoJugador.buscarJugadorPorDireccion("San capi");
//
//            personajes.forEach(System.out::println);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

