package AgregadoPersonaje;

import AgregadoPersonaje.Repositorio.RepoPersonaje;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class mainPersonajes {
    static Scanner teclado = new Scanner(System.in);
    static List<Personaje> personajes = new ArrayList<>();

    public static void main(String[] args) {
        RepoPersonaje repo = null;
        try {
            repo = new RepoPersonaje();
            // Cargar personajes desde JSON
            personajes = new ArrayList<>(repo.findAllToList());
            System.out.println("Se han cargado " + personajes.size() + " personajes del archivo.");

            menuPrincipal(repo);

            // Guardado automático al salir, sobrescribiendo todo
            guardarPersonajes(repo);

        } catch (IOException e) {
            System.err.println("Error al leer o crear el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void menuPrincipal(RepoPersonaje repo) {
        int opcion = 0;
        while (opcion != 12) {
            try {
                System.out.println("\n-------------------------------------------------------");
                System.out.println("Menú Personajes, para salir introduce 13");
                System.out.println("-------------------------------------------------------");
                System.out.println("1. Ver todos los personajes");
                System.out.println("2. Crear personaje");
                System.out.println("3. Buscar personajes por clase");
                System.out.println("4. Buscar personaje por ID opcional");
                System.out.println("5. Buscar personaje por ID");
                System.out.println("6. Eliminar personaje por ID");
                System.out.println("7. Eliminar todos los personajes");
                System.out.println("8. Comprobar si existe personaje por ID");
                System.out.println("9. Contar personajes");
                System.out.println("10. Guardar personajes");
                System.out.println("11. Mostrar iterable de personajes");
                System.out.println("12.Salir");
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

    private static void ejecutarOpcion(int opcion, RepoPersonaje repo) throws Exception {
        switch (opcion) {
            case 1 -> mostrarTodos();
            case 2 -> crearPersonaje(repo);
            case 3 -> buscarPorClase(repo);
            case 4 -> buscarPorIDOpcional(repo);
            case 5 -> buscarPorID(repo);
            case 6 -> eliminarPersonajePorId(repo);
            case 7 -> eliminarTodos(repo);
            case 8 -> comprobarExistencia(repo);
            case 9 -> contarPersonajes(repo);
            case 10 -> guardarPersonajes(repo);
            case 11 -> mostrarIterable(repo);
            case 13 -> System.out.println("Saliendo del menú...");
            default -> System.out.println("Opción no válida.");
        }
    }

    private static void mostrarTodos() {
        if (personajes.isEmpty()) {
            System.out.println("No hay personajes.");
            return;
        }
        personajes.forEach(System.out::println);
    }

    private static void crearPersonaje(RepoPersonaje repo) throws Exception {
        try {
            System.out.println("Introduce nombre del personaje:");
            String nombre = teclado.nextLine();
            System.out.println("Introduce descripción:");
            String descripcion = teclado.nextLine();
            System.out.println("Introduce historia:");
            String historia = teclado.nextLine();
            System.out.println("Introduce capacidad de carga:");
            double carga = teclado.nextDouble();
            teclado.nextLine();

            System.out.println("Introduce clase (Mago, Guerrero, Paladin, Picaro, Druida, Bardo, Clerigo, Ranger):");
            Personaje.Clase clase = Personaje.Clase.valueOf(teclado.nextLine().trim().toUpperCase());

            System.out.println("Introduce raza (HUMANO, ORCO, ELFO, ENANO):");
            Personaje.Raza raza = Personaje.Raza.valueOf(teclado.nextLine().trim().toUpperCase());

            Personaje p = new Personaje(new ArrayList<>(), carga, nombre, descripcion, historia, clase, raza);

            // Se guarda usando repo para asignar ID correctamente
            repo.save(p);

            // Recargar lista actualizada
            personajes = new ArrayList<>(repo.findAllToList());
            System.out.println("Personaje creado:\n" + p);

        } catch (IllegalArgumentException e) {
            System.err.println("Error al crear personaje: " + e.getMessage());
        }
    }

    private static void buscarPorClase(RepoPersonaje repo) {
        try {
            System.out.println("Introduce clase a buscar:");
            Personaje.Clase clase = Personaje.Clase.valueOf(teclado.nextLine().trim().toUpperCase());
            List<Personaje> encontrados = repo.buscarPersonajesPorClases(clase);
            if (encontrados.isEmpty()) {
                System.out.println("No se encontraron personajes con esa clase.");
            } else {
                encontrados.forEach(System.out::println);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Clase inválida");
        }
    }

    private static void buscarPorIDOpcional(RepoPersonaje repo) {
        try {
            System.out.println("Introduce ID del personaje:");
            int id = teclado.nextInt();
            teclado.nextLine();
            Optional<Personaje> p = repo.findByIdOptional(id);
            p.ifPresentOrElse(System.out::println, () -> System.out.println("No se encontró personaje con ID " + id));
        } catch (IllegalArgumentException e) {
            System.err.println("ID inválido");
        }
    }

    private static void buscarPorID(RepoPersonaje repo) throws IOException {
        System.out.println("Introduce ID del personaje:");
        int id = teclado.nextInt();
        teclado.nextLine();
        Personaje p = repo.findById(id);
        System.out.println(p);
    }

    private static void eliminarPersonajePorId(RepoPersonaje repo) throws IOException {
        System.out.println("Introduce ID del personaje a eliminar:");
        int id = teclado.nextInt();
        teclado.nextLine();
        repo.deleteById(id);
        personajes.removeIf(p -> p.getID_PERSONAJE() == id);
        System.out.println("Personaje eliminado.");
    }

    private static void eliminarTodos(RepoPersonaje repo) throws IOException {
        repo.deleteAll();
        personajes.clear();
        System.out.println("Todos los personajes eliminados.");
    }

    private static void comprobarExistencia(RepoPersonaje repo) {
        System.out.println("Introduce ID a comprobar:");
        int id = teclado.nextInt();
        teclado.nextLine();
        System.out.println("Existe ID? " + repo.existsById(id));
    }

    private static void contarPersonajes(RepoPersonaje repo) throws IOException {
        System.out.println("Cantidad de personajes: " + repo.count());
    }

    private static void guardarPersonajes(RepoPersonaje repo) throws IOException {
        try {
            // Borrar todo para evitar duplicados
            repo.deleteAll();

            // Guardar todos los personajes actuales
            for (Personaje p : personajes) {
                repo.save(p);
            }
            System.out.println("Se han guardado todos los personajes correctamente.");
        } catch (Exception e) {
            System.err.println("Error guardando personajes: " + e.getMessage());
        }
    }

    private static void mostrarIterable(RepoPersonaje repo) {
        System.out.println("Iterable de personajes:");
        for (Personaje p : repo.findAll()) {
            System.out.println(p);
        }
    }

}
