package AgregadoJugador;

import AgregadoAventura.Aventura;
import AgregadoJugador.Repositorio.RepoJugador;

import java.io.IOException;
import java.util.ArrayList;

public class cargarJugadores {
    public static void main(String[] args) throws IOException {
        RepoJugador repo = new RepoJugador();
        repo.deleteAll();

        // Direcciones
        DireccionJuego d1 = new DireccionJuego("Madrid", "Gran Vía", "3ºB", "28001");
        DireccionJuego d2 = new DireccionJuego("Barcelona", "Diagonal", "2ºA", "08019");
        DireccionJuego d3 = new DireccionJuego("Sevilla", "Sierpes", "1ºC", "41004");
        DireccionJuego d4 = new DireccionJuego("Valencia", "Colón", "4ºD", "46001");
        DireccionJuego d5 = new DireccionJuego("Bilbao", "Iparraguirre", "5ºE", "48009");
        DireccionJuego d6 = new DireccionJuego("Zaragoza", "Independencia", "6ºF", "50001");
        DireccionJuego d7 = new DireccionJuego("Málaga", "Larios", "7ºA", "29005");
        DireccionJuego d8 = new DireccionJuego("Murcia", "Trapería", "1ºA", "30001");
        DireccionJuego d9 = new DireccionJuego("Salamanca", "Toro", "2ºC", "37002");

        // Direcciones para los directores
        DireccionJuego dd1 = new DireccionJuego("Madrid", "Velázquez", "1ºA", "28001");
        DireccionJuego dd2 = new DireccionJuego("Barcelona", "Rambla", "2ºB", "08002");
        DireccionJuego dd3 = new DireccionJuego("Valencia", "Serranos", "3ºC", "46002");

        // Directores de juego
        DirectorDeJuego dj1 = new DirectorDeJuego("12345678A", "Roberto", dd1, new ArrayList<>());
        Aventura a1 = new Aventura("Clasicos del mazmorreo",3, Aventura.Dificultad.DIFICIL);


        DirectorDeJuego dj2 = new DirectorDeJuego("87654321B", "Elena", dd2);
        DirectorDeJuego dj3 = new DirectorDeJuego("11223344C", "Fernando", dd3);


        // Jugadores
        Jugador j1 = new Jugador("02790162D", "Alex", d1);
        Jugador j2 = new Jugador("12425435G", "María", d2);
        Jugador j3 = new Jugador("12345423D", "Luis", d3);
        Jugador j4 = new Jugador("12342435C", "Claudia", d4);
        Jugador j5 = new Jugador("12342423L", "Diego", d5);
        Jugador j6 = new Jugador("12348923E", "Sofía", d6);
        Jugador j7 = new Jugador("12348982D", "Andrés", d7);
        Jugador j8 = new Jugador("12349123M", "Laura", d8);
        Jugador j9 = new Jugador("12437823F", "Pedro", d9);


        // Añadirlos al repo
        repo.save(j1);
        repo.save(j2);
        repo.save(j3);
        repo.save(j4);
        repo.save(j5);
        repo.save(j6);
        repo.save(j7);
        repo.save(j8);
        repo.save(j9);
        repo.save(dj1);
        repo.save(dj2);
        repo.save(dj3);
    }
}
