package AgregadoAventura;

import AgregadoAventura.Repositorio.RepoAventura;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class mainAventuras {
    static Scanner teclado = new Scanner(System.in);
    static List<Aventura> aventuras = new ArrayList<>();

    public static void main(String[] args) {
        RepoAventura repo;
        try {
            repo = new RepoAventura();

            // Cargar aventuras desde JSON
            aventuras = new ArrayList<>(repo.findAllToList());
            System.out.println("Se han cargado " + aventuras.size() + " aventuras del archivo.");

            menuPrincipal(repo);

            // Guardado automático al salir
            guardarAventuras(repo);

        } catch (IOException e) {
            System.err.println("Error al leer o crear el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void menuPrincipal(RepoAventura repo) {
        int opcion = 0;
        while (opcion != 14) {
            try {
                System.out.println("\n-------------------------------------------------------");
                System.out.println("Menú Aventuras");
                System.out.println("-------------------------------------------------------");
                System.out.println("1. Ver todos los jugadores");
                System.out.println("2. Crear aventura normal");
                System.out.println("3. Crear aventura de acción");
                System.out.println("4. Crear aventura de misterio");
                System.out.println("5. Mostrar todas las aventuras findAllToList");
                System.out.println("6. Mostrar todas las aventuras findAll - Iterable");
                System.out.println("7. Buscar aventura por ID");
                System.out.println("8. Buscar aventuras por dificultad");
                System.out.println("9. Eliminar aventura por ID");
                System.out.println("10. Eliminar todas las aventuras");
                System.out.println("11. Contar aventuras");
                System.out.println("12. Comprobar existencia de ID");
                System.out.println("13. Forzar actualización de datos en archivo");
                System.out.println("14. Salir");
                System.out.println("-------------------------------------------------------");

                opcion = teclado.nextInt();
                teclado.nextLine();
                ejecutarOpcion(opcion, repo);

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

    private static void ejecutarOpcion(int opcion, RepoAventura repo) throws Exception {
        switch (opcion) {
            case 1 -> verTodosLosJugadores();
            case 2 -> crearAventuraNormal(repo);
            case 3 -> crearAventuraAccion(repo);
            case 4 -> crearAventuraMisterio(repo);
            case 5 -> mostrarTodasToList(repo);
            case 6 -> mostrarTodasIterable(repo);
            case 7 -> buscarPorIDOptional(repo);
            case 8 -> buscarPorDificultad(repo);
            case 9 -> eliminarAventuraPorId(repo);
            case 10 -> eliminarTodas(repo);
            case 11 -> contarAventuras(repo);
            case 12 -> comprobarExistenciaId(repo);
            case 13 -> actualizarDatosArchivo(repo);
            case 14 -> System.out.println("Saliendo...");
            default -> System.out.println("Opción no válida.");
        }
    }

    // NUEVA FUNCIÓN DE EJEMPLO: ver todos los jugadores
    private static void verTodosLosJugadores() {
        System.out.println("Ejemplo de todos los jugadores:");
        // Aquí podrías cargar tu lista de jugadores real
        System.out.println("Jugador1, Jugador2, Jugador3...");
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

    // Mostrar
    private static void mostrarTodasToList(RepoAventura repo) {
        System.out.println("Mostrando todas las aventuras:");
        List<Aventura> todas = repo.findAllToList();
        if (todas.isEmpty()) {
            System.out.println("No hay aventuras.");
        } else {
            todas.forEach(System.out::println);
        }
    }

    private static void mostrarTodasIterable(RepoAventura repo) {
        System.out.println("Mostrando todas las aventuras Iterable:");
        for (Aventura a : repo.findAll()) {
            System.out.println(a);
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
    private static void eliminarAventuraPorId(RepoAventura repo) throws IOException {
        System.out.println("Introduce ID de la aventura a eliminar:");
        int id = teclado.nextInt();
        teclado.nextLine();
        repo.deleteById(id);
        aventuras.removeIf(a -> a.getID_AVENTURA() == id);
        System.out.println("Aventura eliminada.");
    }

    private static void eliminarTodas(RepoAventura repo) throws IOException {
        repo.deleteAll();
        aventuras.clear();
        System.out.println("Se han eliminado todas las aventuras.");
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
}
