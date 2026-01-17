package AgregadoPersonaje.Repositorio;

import AgregadoPersonaje.Personaje;

import java.io.IOException;
import java.util.Optional;

public class PruebaTemporal {

    public static void main(String[] args) {
        try {
            RepoPersonaje repoPersonaje = new RepoPersonaje();
            System.out.println(repoPersonaje.count());
            System.out.println(repoPersonaje.existsById(6));


            Optional<Personaje> personaje = repoPersonaje.findByIdOptional(200);
            personaje.ifPresentOrElse(System.out::println, () -> {
                System.out.println("No existe el personaje");
            });

//           List<Personaje> personajes = repoPersonaje.buscarPersonajesPorClases(Personaje.Clase.GUERRERO);
//
//           personajes.forEach(System.out::println);


//            Personaje personaje = repoPersonaje.findById(6);
//
//            System.out.println(personaje);
//
//            Iterable<Personaje> personajes = repoPersonaje.findAll();
//
//            personajes.forEach(System.out::println);


//            repoPersonaje.deleteById(4);
//            System.out.println(repoPersonaje.count());
//            System.out.println(repoPersonaje.existsById(4));
//            repoPersonaje.deleteAll();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
