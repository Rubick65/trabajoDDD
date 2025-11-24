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

            //Se muestra el menu de opciones
            menuPrincipal(repoAventura, repoJugador);


        } catch (IOException e) {
            System.err.println("Error al leer o crear el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     *se muestran las opciones disponibles
     * @param repoAventura repositorio de las aventuras
     * @param repoJugador repositorio de los jugadores
     */
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
                System.out.println("12. Guardar");
                System.out.println("13. Salir");
                System.out.println("-------------------------------------------------------");

                opcion = teclado.nextInt();
                teclado.nextLine();
                ejecutarOpcion(opcion, repoAventura, repoJugador);

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

    /**
     * Se ejecuta una opcion seleccionada del menu
     * @param opcion opcion a ejecutar
     * @param repoAventura repositorio de las aventuras
     * @param repoJugador repositorio de los jugadores
     * @throws Exception excepcion que salta por cualquiera de las otras funciones implementadas en las opciones
     */
    private static void ejecutarOpcion(int opcion, RepoAventura repoAventura, RepoJugador repoJugador) throws Exception {
        switch (opcion) {
            case 1 -> mostrarTodasToList(repoAventura);
            case 2 -> crearAventuraNormal(repoAventura);
            case 3 -> crearAventuraAccion(repoAventura);
            case 4 -> crearAventuraMisterio(repoAventura);
            case 5 -> buscarPorIDOptional(repoAventura);
            case 6 -> sacarConIterable(repoAventura);
            case 7 -> buscarPorDificultad(repoAventura);
            case 8 -> eliminarAventuraPorId(repoAventura, repoJugador);
            case 9 -> eliminarTodas(repoAventura, repoJugador);
            case 10 -> contarAventuras(repoAventura);
            case 11 -> comprobarExistenciaId(repoAventura);
            case 12 -> guardarAventuras(repoAventura);
            case 13 -> System.out.println("Saliendo...");
            default -> System.out.println("Opción no válida.");
        }
    }

    /**
     * Se crea una aventura de tipo normal
     * @param repo repositorio de aventura
     * @throws Exception excepcion que salta en caso de error al introducir los datos
     */
    private static void crearAventuraNormal(RepoAventura repo) throws Exception {
        System.out.println("Introduce nombre de la aventura:");
        String nombre = teclado.nextLine();
        System.out.println("Introduce duración aprox. en sesiones:");
        int duracion = teclado.nextInt();
        teclado.nextLine();
        System.out.println("Introduce dificultad FACIL, NORMAL, DIFICIL:");
        Aventura.Dificultad dificultad = Aventura.Dificultad.valueOf(teclado.nextLine().toUpperCase());

        Aventura a = new Aventura(nombre, duracion, dificultad);
        aventuras.add(a);
        System.out.println("Aventura creada:\n");
    }

    /**
     * Se crea una aventura de tipo accion
     * @param repo repositorio de aventuras
     * @throws Exception excepcion que salta en caso de error al introducir los datos
     */
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
        aventuras.add(a);
        System.out.println("Aventura de acción creada:\n");
    }

    /**
     * Se crea una aventura de tipo misterio
     * @param repo repositorio de aventuras
     * @throws Exception excepcion que salta en caso de error al introducir los datos
     */
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
        aventuras.add(a);
        System.out.println("Aventura de misterio creada:\n");
    }

    /**
     * Se muestran las aventuras con ToList
     * @param repo repositorio donde se cargan las aventuras
     */
    private static void mostrarTodasToList(RepoAventura repo) throws IOException {
        System.out.println("Mostrando todas las aventuras (List):");
        List<Aventura> todas = repo.findAllToList();
        if (todas.isEmpty()) {
            System.out.println("No hay aventuras.");
        } else {
            todas.forEach(System.out::println);
        }
    }

    /**
     * Se muestran las aventuras con Iterable
     * @param repo repositorio donde se cargan las aventuras
     */
    private static void sacarConIterable(RepoAventura repo) {
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

    /**
     * Se busca una aventura mediante id usando Optional
     * @param repo repositorio de aventuras donde se valida si el id existe
     */
    private static void buscarPorIDOptional(RepoAventura repo) throws IOException {
        System.out.println("Introduce ID de la aventura:");
        int id = teclado.nextInt();
        teclado.nextLine();
        repo.findByIdOptional(id).ifPresentOrElse(
                a -> System.out.println("Encontrada:\n" + a),
                () -> System.out.println("No se encontró aventura con ID " + id)
        );
    }

    /**
     * Se busca una funcion mediante la dificultad
     * @param repo repositorio donde se cargan las aventuras con esa dificultad
     */
    private static void buscarPorDificultad(RepoAventura repo) throws IOException {
        System.out.println("Introduce dificultad a buscar FACIL, NORMAL, DIFICIL:");
        Aventura.Dificultad dificultad = Aventura.Dificultad.valueOf(teclado.nextLine().toUpperCase());
        List<Aventura> encontrados = repo.buscarAventuraPorDificultad(dificultad);
        if (encontrados.isEmpty()) {
            System.out.println("No se encontraron aventuras con esa dificultad.");
        } else {
            encontrados.forEach(System.out::println);
        }
    }

    /**
     * Se elimina una aventura mediante su id
     * @param repoAventura repositorio aventuras donde se eliminan las aventuras
     * @param repoJugador repositorio jugadores en donde se eliminan las referencias a aventuras de los directores de juego
     * @throws IOException excepcion lanzada en caso de error al eliminar
     */
    private static void eliminarAventuraPorId(RepoAventura repoAventura, RepoJugador repoJugador) throws IOException {
        System.out.println("Introduce ID de la aventura a eliminar:");
        int id = teclado.nextInt();
        teclado.nextLine();
        repoAventura.deleteById(id);
        aventuras.removeIf(a -> a.getID_AVENTURA() == id);

        // Eliminar la referencia en todos los Directores de Juego
        for (Jugador jugador : repoJugador.findAllToList()) {
            if (jugador instanceof DirectorDeJuego director) {
                try {
                    director.eliminarAventura(id);
                    repoJugador.actualizarDatos(director);
                } catch (Exception ignored) {
                    // La excepción indica que ese director no tenía esa aventura, se ignora
                }
            }
        }
        System.out.println("Aventura eliminada y referencias en Directores de Juego actualizadas.");
    }

    /**
     * Se eliminan todas las aventuras
     * @param repoAventura repositorio donde se eliminan las aventuras
     * @param repoJugador repositorio que borra las referencias de todas las aventuras de los directores de juego
     * @throws IOException excepcion que salta en caso de error al borrar
     */
    private static void eliminarTodas(RepoAventura repoAventura, RepoJugador repoJugador) throws IOException {
        repoAventura.deleteAll();
        aventuras.clear();

        // Eliminar todas las referencias de aventuras en todos los Directores de Juego
        for (Jugador jugador : repoJugador.findAllToList()) {
            if (jugador instanceof DirectorDeJuego director) {
                for (int idAventura : new ArrayList<>(director.getListaAventuras())) {
                    try {
                        director.eliminarAventura(idAventura);
                        repoJugador.actualizarDatos(director);
                    } catch (Exception ignored) {
                        // Si por alguna razón no existía, se ignora
                    }
                }
            }
        }
        System.out.println("Se han eliminado todas las aventuras y todas las referencias en Directores de Juego.");
    }

    /**
     * cuenta la cantidad de aventuras existentes
     * @param repo repositorio de aventuras
     * @throws IOException excepcion por si la lista no contiene nada
     */
    private static void contarAventuras(RepoAventura repo) throws IOException {
        System.out.println("Cantidad de aventuras: " + repo.count());
    }

    /**
     * Se comprueba la existencia de una aventura mediante un id
     * @param repo repositorio de aventuras
     */
    private static void comprobarExistenciaId(RepoAventura repo) throws IOException {
        System.out.println("Introduce ID a comprobar:");
        int id = teclado.nextInt();
        teclado.nextLine();
        boolean existe = repo.existsById(id);
        System.out.println("Existe ID " + id + ": " + existe);
    }

    /**
     * Se guardan los cambios
     * @param repoAventura repositorio donde se guardan las aventuras
     */
    private static void guardarAventuras(RepoAventura repoAventura) {
        if (aventuras.isEmpty()) {
            System.out.println("Aún no has creado ninguna aventura");
            return;
        }

        for (Aventura aventura : aventuras) {
            try {
                repoAventura.save(aventura);
                System.out.println("La aventura " + aventura.getNombreAventura() + " se ha guardado");
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Se ha terminado el guardado");
    }
}
