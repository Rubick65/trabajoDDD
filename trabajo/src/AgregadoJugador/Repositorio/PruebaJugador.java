package AgregadoJugador.Repositorio;

import AgregadoAventura.Aventura;
import AgregadoJugador.DireccionJuego;
import AgregadoJugador.DirectorDeJuego;
import AgregadoJugador.Jugador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PruebaJugador {
    public static void main(String[] args) {
        try {
            RepoJugador repoJugador = new RepoJugador();
            System.out.println(repoJugador.count());
            System.out.println(repoJugador.existsById(6));


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


            DireccionJuego direccionJuego = new DireccionJuego("Luna", "San capi", "1C", "29024");
            DirectorDeJuego jugador = new DirectorDeJuego("02790162D", "Jose", direccionJuego );

            repoJugador.save(jugador);

            Jugador jugador1 = repoJugador.findById(20);

            System.out.println(jugador1);

            Optional<Jugador> personaje = repoJugador.findByIdOptional(21);

            personaje.ifPresentOrElse(System.out::println, () -> System.out.println("No existe el personaje"));

            List<Jugador> personajes = repoJugador.buscarJugadorPorDireccion("San capi");

            personajes.forEach(System.out::println);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

