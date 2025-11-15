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
        RepoAventura repo = null;
        try {
            repo = new RepoAventura();
            // Cargar aventuras desde JSON
            aventuras = new ArrayList<>(repo.findAllToList());
            System.out.println("Se han cargado " + aventuras.size() + " aventuras del archivo.");

            menuPrincipal(repo);

            // Guardar automáticamente al salir
            repo.deleteAll();
            for (Aventura a : aventuras) {
                repo.save(a);
            }
            System.out.println("Todas las aventuras se han guardado correctamente al salir.");

        } catch (IOException e) {
            System.err.println("Error al leer o crear el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void menuPrincipal(RepoAventura repo) {
        int opcion = 0;
        while (opcion != 9) { // Cambiado a 9 para salir
            try {
                System.out.println("\n-------------------------------------------------------");
                System.out.println("Menú Aventuras, para salir introduce 9");
                System.out.println("-------------------------------------------------------");
                System.out.println("1. Crear aventura normal");
                System.out.println("2. Crear aventura de acción");
                System.out.println("3. Crear aventura de misterio");
                System.out.println("4. Mostrar todas las aventuras");
                System.out.println("5. Buscar aventura por ID");
                System.out.println("6. Buscar aventuras por dificultad");
                System.out.println("7. Eliminar aventura por ID");
                System.out.println("8. Mostrar cantidad de aventuras");
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

    private static void ejecutarOpcion(int opcion, RepoAventura repo) throws IOException, Exception {
        switch (opcion) {
            case 1 -> crearAventuraNormal(repo);
            case 2 -> crearAventuraAccion(repo);
            case 3 -> crearAventuraMisterio(repo);
            case 4 -> mostrarTodas();
            case 5 -> buscarPorID(repo);
            case 6 -> buscarPorDificultad(repo);
            case 7 -> eliminarAventuraPorId(repo);
            case 8 -> System.out.println("Cantidad de aventuras: " + aventuras.size());
            case 9 -> System.out.println("Saliendo del menú...");
            default -> System.out.println("Opción no válida.");
        }
    }

    private static void crearAventuraNormal(RepoAventura repo) throws Exception {
        System.out.println("Introduce nombre de la aventura:");
        String nombre = teclado.nextLine();
        System.out.println("Introduce duración aprox. en sesiones:");
        int duracion = teclado.nextInt();
        teclado.nextLine();
        System.out.println("Introduce dificultad (FACIL, NORMAL, DIFICIL):");
        Aventura.Dificultad dificultad = Aventura.Dificultad.valueOf(teclado.nextLine().toUpperCase());

        Aventura a = new Aventura(nombre, duracion, dificultad);
        repo.save(a); // ID automático asignado por el repositorio
        aventuras.add(a);
        System.out.println("Aventura creada:\n" + a);
    }

    private static void crearAventuraAccion(RepoAventura repo) throws Exception {
        System.out.println("Introduce nombre de la aventura de acción:");
        String nombre = teclado.nextLine();
        System.out.println("Introduce duración aprox. en sesiones:");
        int duracion = teclado.nextInt();
        teclado.nextLine();
        System.out.println("Introduce dificultad (FACIL, NORMAL, DIFICIL):");
        Aventura.Dificultad dificultad = Aventura.Dificultad.valueOf(teclado.nextLine().toUpperCase());
        System.out.println("Introduce cantidad de enemigos:");
        int enemigos = teclado.nextInt();
        teclado.nextLine();
        System.out.println("Introduce cantidad de ubicaciones:");
        int ubicaciones = teclado.nextInt();
        teclado.nextLine();

        AventuraAccion a = new AventuraAccion(nombre, duracion, dificultad, enemigos, ubicaciones);
        repo.save(a); // ID automático asignado por el repositorio
        aventuras.add(a);
        System.out.println("Aventura de acción creada:\n" + a);
    }

    private static void crearAventuraMisterio(RepoAventura repo) throws Exception {
        System.out.println("Introduce nombre de la aventura de misterio:");
        String nombre = teclado.nextLine();
        System.out.println("Introduce duración aprox. en sesiones:");
        int duracion = teclado.nextInt();
        teclado.nextLine();
        System.out.println("Introduce dificultad (FACIL, NORMAL, DIFICIL):");
        Aventura.Dificultad dificultad = Aventura.Dificultad.valueOf(teclado.nextLine().toUpperCase());
        System.out.println("Introduce enigma principal:");
        String enigma = teclado.nextLine();

        AventuraMisterio a = new AventuraMisterio(nombre, duracion, dificultad, enigma);
        repo.save(a); // ID automático asignado por el repositorio
        aventuras.add(a);
        System.out.println("Aventura de misterio creada:\n" + a);
    }

    private static void mostrarTodas() {
        if (aventuras.isEmpty()) {
            System.out.println("No hay aventuras.");
            return;
        }
        aventuras.forEach(System.out::println);
    }

    private static void buscarPorID(RepoAventura repo) {
        try {
            System.out.println("Introduce ID de la aventura:");
            int id = teclado.nextInt();
            teclado.nextLine();
            Aventura a = repo.findByIdOptional(id).orElse(null);
            if (a == null) {
                System.out.println("No se encontró aventura con ID " + id);
            } else {
                System.out.println(a);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("ID inválido");
        }
    }

    private static void buscarPorDificultad(RepoAventura repo) {
        try {
            System.out.println("Introduce dificultad a buscar (FACIL, NORMAL, DIFICIL):");
            Aventura.Dificultad dificultad = Aventura.Dificultad.valueOf(teclado.nextLine().toUpperCase());
            List<Aventura> encontrados = repo.buscarAventuraPorDificultad(dificultad);
            if (encontrados.isEmpty()) {
                System.out.println("No se encontraron aventuras con esa dificultad.");
            } else {
                encontrados.forEach(System.out::println);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Dificultad inválida");
        }
    }

    private static void eliminarAventuraPorId(RepoAventura repo) throws IOException {
        System.out.println("Introduce ID de la aventura a eliminar:");
        int id = teclado.nextInt();
        teclado.nextLine();
        repo.deleteById(id);
        aventuras.removeIf(a -> a.getID_AVENTURA() == id);
        System.out.println("Aventura eliminada.");
    }
}
