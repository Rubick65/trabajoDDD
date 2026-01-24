package AgregadoPersonaje.Repositorio;

import AgregadoPersonaje.ObjetoInventario;
import AgregadoPersonaje.Personaje;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PruebaTemporal {

    public static void main(String[] args) {
        try {
            RepoPersonaje repoPersonaje = new RepoPersonaje();
            System.out.println(repoPersonaje.count());
            System.out.println(repoPersonaje.existsById(6));

//            List<ObjetoInventario> objetos = new ArrayList<>();
//
//            objetos.add(new ObjetoInventario("Palo", 20.5, "Un palo muy pesado"));
//
//
//            Personaje personaje = new Personaje(15, objetos, 250, "Jose Manuel", "Completamente esquizofrénico", "Enano fan de la minería", Personaje.Clase.MAGO, Personaje.Raza.ENANO);
//
//            repoPersonaje.save(personaje);

            Personaje personajeNuevo = repoPersonaje.findById(17);

            System.out.println(personajeNuevo);


//            List<ObjetoInventario> inventario, double capacidadCarga, String nombrePersonaje, String descripcion, String historia, Personaje.Clase
//            clase, Personaje.Raza raza


//            Optional<Personaje> personaje = repoPersonaje.findByIdOptional(3);
//
//            personaje.ifPresentOrElse(System.out::println, () -> {
//                System.out.println("No existe el personaje");
//            });

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
