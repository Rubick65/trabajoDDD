package AgregadoAventura;

import AgregadoAventura.Repositorio.RepoAventura;
import AgregadoJugador.DirectorDeJuego;
import AgregadoJugador.Jugador;
import AgregadoJugador.Repositorio.RepoJugador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class mainAventuras {
    static Scanner teclado = new Scanner(System.in);
    static List<Aventura> aventuras = new ArrayList<>();
    static List<Jugador> jugadores = new ArrayList<>();

    public static void main(String[] args) {
        RepoAventura repoAventura;
        RepoJugador repoJugador;
        try {
            repoAventura = new RepoAventura();
            repoJugador = new RepoJugador();

            // Cargar aventuras desde JSON
            aventuras = new ArrayList<>(repoAventura.findAllToList());
            jugadores = new ArrayList<>(repoJugador.findAllToList());
            System.out.println("Se han cargado " + aventuras.size() + " aventuras del archivo.");

            menuPrincipal(repoAventura,repoJugador);

            // Guardado automático al salir
            guardarAventuras(repoAventura);
            guardarJugador(repoJugador);

        } catch (IOException e) {
            System.err.println("Error al leer o crear el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void menuPrincipal(RepoAventura repoAventura, RepoJugador repoJugador) {
        int opcion = 0;
        while (opcion != 13) {
            try {
                System.out.println("\n-------------------------------------------------------");
                System.out.println("Menú Aventuras");
                System.out.println("-------------------------------------------------------");
                System.out.println("1. Mostrar aventuras");
                System.out.println("2. Crear aventura normal");
                System.out.println("3. Crear aventura de acción");
                System.out.println("4. Crear aventura de misterio");
                System.out.println("5. Buscar aventura por ID");
                System.out.println("6. Mostrar aventuras con iterable");
                System.out.println("7. Buscar aventuras por dificultad");
                System.out.println("8. Eliminar aventura por ID");
                System.out.println("9. Eliminar todas las aventuras");
                System.out.println("10. Contar aventuras");
                System.out.println("11. Comprobar existencia de ID");
                System.out.println("12. Forzar actualización de datos en archivo");
                System.out.println("13. Salir");
                System.out.println("-------------------------------------------------------");

                opcion = teclado.nextInt();
                teclado.nextLine();
                ejecutarOpcion(opcion, repoAventura,repoJugador);

            } catch (InputMismatchException e) {
                System.err.println("Introduce solo números válidos.");
                teclado.nextLine();
            } catch (IllegalArgumentException e) {
                System.err.println("Error: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Error de archivo: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void ejecutarOpcion(int opcion, RepoAventura repoAventura, RepoJugador repoJugador) throws Exception {
        switch (opcion) {
            case 1 -> mostrarTodasToList(repoAventura);
            case 2 -> crearAventuraNormal(repoAventura);
            case 3 -> crearAventuraAccion(repoAventura);
            case 4 -> crearAventuraMisterio(repoAventura);
            case 5 -> buscarPorIDOptional(repoAventura);
            case 6 -> mostrarTodasIterableCorrecto(repoAventura);
            case 7 -> buscarPorDificultad(repoAventura);
            case 8 -> eliminarAventuraPorId(repoAventura,repoJugador);
            case 9 -> eliminarTodas(repoAventura,repoJugador);
            case 10 -> contarAventuras(repoAventura);
            case 11 -> comprobarExistenciaId(repoAventura);
            case 12 -> actualizarDatosArchivo(repoAventura);
            case 13 -> System.out.println("Saliendo...");
            default -> System.out.println("Opción no válida.");
        }
    }

    // Crear aventuras
    private static void crearAventuraNormal(RepoAventura repo) throws Exception {
        System.out.println("Introduce nombre de la aventura:");
        String nombre = teclado.nextLine();
        System.out.println("Introduce duración aprox. en sesiones:");
        int duracion = teclado.nextInt();
        teclado.nextLine();
        System.out.println("Introduce dificultad FACIL, NORMAL, DIFICIL:");
        Aventura.Dificultad dificultad = Aventura.Dificultad.valueOf(teclado.nextLine().toUpperCase());

        Aventura a = new Aventura(nombre, duracion, dificultad);
        repo.save(a);
        aventuras.add(a);
        System.out.println("Aventura creada:\n" + a);
    }

    private static void crearAventuraAccion(RepoAventura repo) throws Exception {
        System.out.println("Introduce nombre de la aventura de acción:");
        String nombre = teclado.nextLine();
        System.out.println("Introduce duración aprox. en sesiones:");
        int duracion = teclado.nextInt();
        teclado.nextLine();
        System.out.println("Introduce dificultad FACIL, NORMAL, DIFICIL:");
        Aventura.Dificultad dificultad = Aventura.Dificultad.valueOf(teclado.nextLine().toUpperCase());
        System.out.println("Introduce cantidad de enemigos:");
        int enemigos = teclado.nextInt();
        teclado.nextLine();
        System.out.println("Introduce cantidad de ubicaciones:");
        int ubicaciones = teclado.nextInt();
        teclado.nextLine();

        AventuraAccion a = new AventuraAccion(nombre, duracion, dificultad, enemigos, ubicaciones);
        repo.save(a);
        aventuras.add(a);
        System.out.println("Aventura de acción creada:\n" + a);
    }

    private static void crearAventuraMisterio(RepoAventura repo) throws Exception {
        System.out.println("Introduce nombre de la aventura de misterio:");
        String nombre = teclado.nextLine();
        System.out.println("Introduce duración aprox. en sesiones:");
        int duracion = teclado.nextInt();
        teclado.nextLine();
        System.out.println("Introduce dificultad FACIL, NORMAL, DIFICIL:");
        Aventura.Dificultad dificultad = Aventura.Dificultad.valueOf(teclado.nextLine().toUpperCase());
        System.out.println("Introduce enigma principal:");
        String enigma = teclado.nextLine();

        AventuraMisterio a = new AventuraMisterio(nombre, duracion, dificultad, enigma);
        repo.save(a);
        aventuras.add(a);
        System.out.println("Aventura de misterio creada:\n" + a);
    }

    // Mostrar aventuras con List
    private static void mostrarTodasToList(RepoAventura repo) {
        System.out.println("Mostrando todas las aventuras (List):");
        List<Aventura> todas = repo.findAllToList();
        if (todas.isEmpty()) {
            System.out.println("No hay aventuras.");
        } else {
            todas.forEach(System.out::println);
        }
    }

    // Mostrar aventuras con Iterable
    private static void mostrarTodasIterableCorrecto(RepoAventura repo) {
        System.out.println("Mostrando todas las aventuras (Iterable):");
        try {
            Iterable<Aventura> iterable = repo.findAll();
            boolean hay = false;
            for (Aventura a : iterable) {
                System.out.println(a);
                hay = true;
            }
            if (!hay) {
                System.out.println("No hay aventuras.");
            }
        } catch (Exception e) {
            System.err.println("Error mostrando aventuras con Iterable: " + e.getMessage());
        }
    }

    // Buscar
    private static void buscarPorIDOptional(RepoAventura repo) {
        System.out.println("Introduce ID de la aventura:");
        int id = teclado.nextInt();
        teclado.nextLine();
        repo.findByIdOptional(id).ifPresentOrElse(
                a -> System.out.println("Encontrada:\n" + a),
                () -> System.out.println("No se encontró aventura con ID " + id)
        );
    }

    private static void buscarPorDificultad(RepoAventura repo) {
        System.out.println("Introduce dificultad a buscar FACIL, NORMAL, DIFICIL:");
        Aventura.Dificultad dificultad = Aventura.Dificultad.valueOf(teclado.nextLine().toUpperCase());
        List<Aventura> encontrados = repo.buscarAventuraPorDificultad(dificultad);
        if (encontrados.isEmpty()) {
            System.out.println("No se encontraron aventuras con esa dificultad.");
        } else {
            encontrados.forEach(System.out::println);
        }
    }

    // Eliminar
    private static void eliminarAventuraPorId(RepoAventura repoAventura,RepoJugador repoJugador) throws IOException {
        System.out.println("Introduce ID de la aventura a eliminar:");
        int id = teclado.nextInt();
        teclado.nextLine();
        repoAventura.deleteById(id);
        aventuras.removeIf(a -> a.getID_AVENTURA() == id);

        // Eliminar la referencia en todos los Directores de Juego
        for (Jugador jugador : repoJugador.findAllToList()) {
            if (jugador instanceof DirectorDeJuego director) {
                try {
                    director.eliminarAventura(id); // Usa el método que ya lanza excepción si no existe
                } catch (Exception ignored) {
                    // La excepción indica que ese director no tenía esa aventura, se ignora
                }
            }
        }
        System.out.println("Aventura eliminada y referencias en Directores de Juego actualizadas.");
    }

    private static void eliminarTodas(RepoAventura repoAventura,RepoJugador repoJugador) throws IOException {
        repoAventura.deleteAll();
        aventuras.clear();

        // Eliminar todas las referencias de aventuras en todos los Directores de Juego
        for (Jugador jugador : repoJugador.findAllToList()) {
            if (jugador instanceof DirectorDeJuego director) {
                for (int idAventura : new ArrayList<>(director.getListaAventuras())) {
                    try {
                        director.eliminarAventura(idAventura);
                    } catch (Exception ignored) {
                        // Si por alguna razón no existía, se ignora
                    }
                }
            }
        }
        System.out.println("Se han eliminado todas las aventuras y todas las referencias en Directores de Juego.");
    }

    // Otras funciones del repo
    private static void contarAventuras(RepoAventura repo) throws IOException {
        System.out.println("Cantidad de aventuras: " + repo.count());
    }

    private static void comprobarExistenciaId(RepoAventura repo) {
        System.out.println("Introduce ID a comprobar:");
        int id = teclado.nextInt();
        teclado.nextLine();
        boolean existe = repo.existsById(id);
        System.out.println("Existe ID " + id + ": " + existe);
    }

    private static void actualizarDatosArchivo(RepoAventura repo) throws IOException {
        repo.actualizarDatos();
        System.out.println("Datos actualizados en archivo manualmente.");
    }

    private static void guardarAventuras(RepoAventura repo) {
        try {
            repo.deleteAll();
            for (Aventura a : aventuras) {
                repo.save(a);
            }
            System.out.println("Guardado completado.");
        } catch (Exception e) {
            System.err.println("Error guardando aventuras: " + e.getMessage());
        }
    }

    private static void guardarJugador(RepoJugador repoJugador) throws IOException {
        if (jugadores.isEmpty()) {
            System.out.println("Aún no has creado ningún jugador");
            return;
        }
        System.out.println("Vamos a intentar guardar todos los jugadores que has creado");
        for (Jugador jugador : jugadores) {
            try {
                repoJugador.save(jugador);
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }


            System.out.println("Se ha terminado el guardado");
        }
        jugadores.clear();
    }
}
