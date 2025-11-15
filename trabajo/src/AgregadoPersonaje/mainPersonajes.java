package AgregadoPersonaje;

import AgregadoPersonaje.Repositorio.RepoPersonaje;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class mainPersonajes {
    static Scanner teclado = new Scanner(System.in);
    static List<Personaje> personajes = new ArrayList<>(); // Lista local que refleja los del repo

    public static void main(String[] args) {
        RepoPersonaje repo = null;
        try {
            repo = new RepoPersonaje();
            // Cargar personajes existentes desde JSON
            personajes = new ArrayList<>(repo.findAllToList());
            System.out.println("Se han cargado " + personajes.size() + " personajes del archivo.");

            menuPrincipal(repo);

            // Guardar automáticamente al salir, sobrescribiendo todo
            guardarPersonajesSobrescribiendo(repo);

        } catch (IOException e) {
            System.err.println("Error al leer o crear el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void menuPrincipal(RepoPersonaje repo) {
        int opcion = 0;
        while (opcion != 13) {
            try {
                System.out.println("\n-------------------------------------------------------");
                System.out.println("Menú Personajes, para salir introduce 13");
                System.out.println("-------------------------------------------------------");
                System.out.println("1.Crear personaje");
                System.out.println("2.Mostrar todos los personajes");
                System.out.println("3.Mostrar inventario de un personaje");
                System.out.println("4.Agregar objeto al inventario");
                System.out.println("5.Tirar objeto del inventario");
                System.out.println("6.Ordenar inventario por nombre");
                System.out.println("7.Ordenar inventario por peso");
                System.out.println("8.Buscar personaje por clase");
                System.out.println("9.Buscar personaje por ID");
                System.out.println("10.Guardar personajes");
                System.out.println("11.Eliminar personaje por ID");
                System.out.println("12.Mostrar cantidad de personajes");
                System.out.println("-------------------------------------------------------");

                opcion = teclado.nextInt();
                teclado.nextLine(); // Limpiar buffer
                ejecutarOpcion(opcion, repo);

            } catch (InputMismatchException e) {
                System.err.println("Introduce solo números válidos.");
                teclado.nextLine();
            } catch (IllegalArgumentException e) {
                System.err.println("Error: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Error de archivo: " + e.getMessage());
            }
        }
    }

    private static void ejecutarOpcion(int opcion, RepoPersonaje repo) throws IOException {
        switch (opcion) {
            case 1 -> crearPersonaje();
            case 2 -> mostrarTodos();
            case 3 -> mostrarInventario();
            case 4 -> agregarObjeto();
            case 5 -> tirarObjeto();
            case 6 -> ordenarInventarioNombre();
            case 7 -> ordenarInventarioPeso();
            case 8 -> buscarPorClase(repo);
            case 9 -> buscarPorID(repo);
            case 10 -> guardarPersonajesSobrescribiendo(repo);
            case 11 -> eliminarPersonajePorId(repo);
            case 12 -> System.out.println("Cantidad de personajes: " + personajes.size());
            case 13 -> System.out.println("Saliendo del menú...");
            default -> System.out.println("Opción no válida.");
        }
    }

    private static void crearPersonaje() {
        try {
            System.out.println("Introduce nombre del personaje:");
            String nombre = teclado.nextLine();
            System.out.println("Introduce descripción:");
            String descripcion = teclado.nextLine();
            System.out.println("Introduce historia:");
            String historia = teclado.nextLine();

            System.out.println("Introduce clase (MAGO, GUERRERO, PALADIN, PICARO, DRUIDA, BARDO, CLERIGO, RANGER):");
            Personaje.Clase clase = Personaje.Clase.valueOf(teclado.nextLine().toUpperCase());

            System.out.println("Introduce raza (HUMANO, ORCO, ELFO, ENANO):");
            Raza raza = Raza.valueOf(teclado.nextLine().toUpperCase());

            Personaje p = new Personaje(new ArrayList<>(), 100, nombre, descripcion, historia, clase, raza);
            personajes.add(p);
            System.out.println("Personaje creado:\n" + p);
        } catch (IllegalArgumentException e) {
            System.err.println("Error al crear personaje: " + e.getMessage());
        }
    }

    private static Personaje seleccionarPersonaje() {
        if (personajes.isEmpty()) {
            System.out.println("No hay personajes disponibles.");
            return null;
        }
        System.out.println("Selecciona personaje por índice (0 a " + (personajes.size() - 1) + "):");
        for (int i = 0; i < personajes.size(); i++) {
            System.out.println(i + ". " + personajes.get(i).getNombrePersonaje());
        }
        int idx = teclado.nextInt();
        teclado.nextLine();
        if (idx < 0 || idx >= personajes.size()) {
            System.err.println("Índice fuera de rango");
            return null;
        }
        return personajes.get(idx);
    }

    private static void mostrarInventario() {
        Personaje p = seleccionarPersonaje();
        if (p != null)
            System.out.println("Inventario de " + p.getNombrePersonaje() + ": " + p.revisarInventario());
    }

    private static void agregarObjeto() {
        Personaje p = seleccionarPersonaje();
        if (p == null) return;

        try {
            System.out.println("Introduce nombre del objeto:");
            String nombre = teclado.nextLine();
            System.out.println("Introduce descripción del objeto:");
            String descripcion = teclado.nextLine();
            System.out.println("Introduce peso:");
            double peso = teclado.nextDouble();
            teclado.nextLine();

            p.agregarObjeto(new ObjetoInventario(nombre, peso, descripcion));
            System.out.println("Objeto agregado.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error al agregar objeto: " + e.getMessage());
        }
    }

    private static void tirarObjeto() {
        Personaje p = seleccionarPersonaje();
        if (p == null) return;

        if (p.getInventario().isEmpty()) {
            System.out.println("Inventario vacío.");
            return;
        }

        System.out.println("Introduce el índice del objeto a tirar:");
        for (int i = 0; i < p.getInventario().size(); i++) {
            System.out.println(i + ". " + p.getInventario().get(i).getNombre());
        }
        int idx = teclado.nextInt();
        teclado.nextLine();
        if (idx < 0 || idx >= p.getInventario().size()) {
            System.err.println("Índice fuera de rango");
            return;
        }
        p.tirarObjeto(p.getInventario().get(idx));
        System.out.println("Objeto tirado.");
    }

    private static void ordenarInventarioNombre() {
        Personaje p = seleccionarPersonaje();
        if (p != null) {
            p.ordenarInventarioPorNombre();
            System.out.println("Inventario ordenado por nombre.");
        }
    }

    private static void ordenarInventarioPeso() {
        Personaje p = seleccionarPersonaje();
        if (p != null) {
            p.ordenarInventarioPorPeso();
            System.out.println("Inventario ordenado por peso.");
        }
    }

    private static void buscarPorClase(RepoPersonaje repo) {
        System.out.println("Introduce clase a buscar:");
        try {
            Personaje.Clase clase = Personaje.Clase.valueOf(teclado.nextLine().toUpperCase());
            List<Personaje> encontrados = repo.buscarPersonajesPorClases(clase);
            if (encontrados.isEmpty()) {
                System.out.println("No se encontraron personajes de esa clase.");
            } else {
                encontrados.forEach(System.out::println);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Clase inválida");
        }
    }

    private static void buscarPorID(RepoPersonaje repo) {
        System.out.println("Introduce ID del personaje:");
        int id = teclado.nextInt();
        teclado.nextLine();
        Personaje p = repo.findByIdOptional(id).orElse(null);
        if (p == null) {
            System.out.println("No se encontró personaje con ID " + id);
        } else {
            System.out.println(p);
        }
    }

    private static void guardarPersonajesSobrescribiendo(RepoPersonaje repo) {
        try {
            // Borrar todos y guardar la lista actual
            repo.deleteAll();
            for (Personaje p : personajes) {
                repo.save(p);
            }
            System.out.println("Guardado completado (sobrescrito).");
        } catch (Exception e) {
            System.err.println("Error guardando personajes: " + e.getMessage());
        }
    }

    private static void eliminarPersonajePorId(RepoPersonaje repo) throws IOException {
        System.out.println("Introduce ID del personaje a eliminar:");
        int id = teclado.nextInt();
        teclado.nextLine();
        repo.deleteById(id);
        personajes.removeIf(p -> p.getID_PERSONAJE() == id);
        System.out.println("Personaje eliminado.");
    }

    private static void mostrarTodos() {
        if (personajes.isEmpty()) {
            System.out.println("No hay personajes.");
            return;
        }
        personajes.forEach(System.out::println);
    }
}
