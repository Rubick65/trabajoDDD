package AgregadoGrupoJuego;

import AgregadoGrupoJuego.Repositorio.RepoGrupoJuego;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class mainGrupoJuego {
    static Scanner teclado = new Scanner(System.in);
    static List<GrupoJuego> grupoJugs = new ArrayList<>();

    public static void main(String[] args) {
        try {
            RepoGrupoJuego repoGrupoJuego = new RepoGrupoJuego();
            repoGrupoJuego.deleteAll();
            menuPrincipal(repoGrupoJuego);
        } catch (IOException e) {
            System.err.println("Fallo a la hora de leer el archivo " + e.getMessage());
        }

    }

    /**
     * Función que muestra el menú principal al usuario
     *
     * @param repoGrupoJuego Repositorio del grupo de juego
     */
    private static void menuPrincipal(RepoGrupoJuego repoGrupoJuego) {
        int opcionJugador = 0;
        while (opcionJugador != 12) {
            try {
                System.out.println("-------------------------------------------------------");
                System.out.println("Menú, para salir introduce 12");
                System.out.println("-------------------------------------------------------");
                System.out.println("1.Crear Grupo de juego");
                System.out.println("2.Buscar Grupo de juego por id opcional");
                System.out.println("3.Sacar Grupo de juego a lista");
                System.out.println("4.Contar Grupo de juego");
                System.out.println("5.Eliminar Grupo de juego");
                System.out.println("6.Eliminar todos los Grupos de juego");
                System.out.println("7.Comprobar si un Grupo de juego existe");
                System.out.println("8.Buscar Grupo de juego por id");
                System.out.println("9.Sacar todos los Grupos de juego a un Iterable");
                System.out.println("10.Guardar Grupos de juego");
                System.out.println("11.Filtrar grupos de juego por jugador");
                System.out.println("12.Salir");
                System.out.println("-------------------------------------------------------");

                opcionJugador = teclado.nextInt();
                opcionesPrincipales(opcionJugador, repoGrupoJuego);

            } catch (InputMismatchException e) {
                System.err.println("Introduce solo números del 1-11 " + e.getMessage());
                teclado.nextLine();
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            } catch (IOException e) {
                System.err.println("Error al acceder al archivo");
            }

        }
    }

    /**
     * Función que indica que función del repo se va a ejecutar
     *
     * @param opcion         Opción que selecciona el usuario
     * @param repoGrupoJuego Repositorio del grupo de juego
     * @throws IllegalArgumentException Lanza excepción en caso de que se introduzcan datos incorrectos
     * @throws IOException              Lanza la excepción en caso de problemas con lectura o escritura de los archivos
     */
    private static void opcionesPrincipales(int opcion, RepoGrupoJuego repoGrupoJuego) throws IllegalArgumentException, IOException {
        switch (opcion) {
            case 1:
                crearGrupoJuego();
                break;
            case 2:
                buscarPorIDOpcional(repoGrupoJuego);
                break;
            case 3:
                List<GrupoJuego> listaGrupos = repoGrupoJuego.findAllToList();
                for (GrupoJuego jugador : listaGrupos) {
                    System.out.println(jugador);
                }
                if (listaGrupos.isEmpty())
                    System.out.println("La lista está vacía");
                break;
            case 4:
                System.out.println("La cantidad de jugadores es: " + repoGrupoJuego.count());
                break;
            case 5:
                eliminarGrupoJuegoPorId(repoGrupoJuego);
                break;
            case 6:
                eliminarTodosLosGruposJuego(repoGrupoJuego);
                break;
            case 7:
                comprobarSiGrupoJuegoExiste(repoGrupoJuego);
                break;
            case 8:
                buscarGrupoJuegoPorID(repoGrupoJuego);
                break;
            case 9:
                sacarGrupoJuegoIterable(repoGrupoJuego);
                break;
            case 10:
                guardarGrupoJuego(repoGrupoJuego);
                break;
            case 11:
                buscarGruposPorJugador(repoGrupoJuego);
                break;
            default:
                break;
        }

    }

    /**
     * Función que permite crear un grupo de juego
     *
     * @throws IOException Lanza excepción en caso de problemas a la hora de leer el archivo
     */
    private static void crearGrupoJuego() throws IOException {
        String nombreGrupo, descripcion;
        int jugadorId = 1;
        while (true) {
            try {
                List<Integer> listaJugadores = new ArrayList<>();
                teclado.nextLine();
                System.out.println("Vamos a crear un grupo de juego:");
                System.out.println("Introduce el nombre del grupo de juego: ");
                nombreGrupo = teclado.nextLine();
                System.out.println("Ahora introduce la descripción del grupo: ");
                descripcion = teclado.nextLine();
                System.out.println("Por último introduce la lista de jugadores para terminar pulsa 0: ");
                while (true) {
                    jugadorId = teclado.nextInt();
                    if (jugadorId == 0)
                        break;
                    listaJugadores.add(jugadorId);
                }
                grupoJugs.add(new GrupoJuego(nombreGrupo, descripcion, listaJugadores));
                break;
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());

            }

        }

    }

    /**
     * Función que busca al grupo de juego en el archivo
     *
     * @param repoGrupoJuego Repositorio del grupo de juego
     * @throws IllegalArgumentException Lanza excepción si el id pasado no existe
     */
    private static void buscarPorIDOpcional(RepoGrupoJuego repoGrupoJuego) throws IllegalArgumentException, IOException {
        int idGrupoJugador;
        System.out.println("Introduce el id del grupo de juego que quieras buscar:");
        idGrupoJugador = teclado.nextInt();
        System.out.println(repoGrupoJuego.findByIdOptional(idGrupoJugador));

    }

    /**
     * Función que elimina un grupo por ID
     *
     * @param repoGrupoJuego Repositorio del grupo de juego
     * @throws IOException              Lanza excepción en caso de problemas con la lectura o escritura de archivos
     * @throws IllegalArgumentException En caso de ser el id no válido o no pertenezca a ningún jugador
     */
    private static void eliminarGrupoJuegoPorId(RepoGrupoJuego repoGrupoJuego) throws IOException, IllegalArgumentException {
        int idJugador;
        System.out.println("Introduce el id del grupo de juego que quieras eliminar:");
        idJugador = teclado.nextInt();
        repoGrupoJuego.deleteById(idJugador);
        System.out.println("El grupo de juego ha sido eliminado con éxito");
    }

    /**
     * Función que elimina a todos los grupos de juego
     *
     * @param repoGrupoJuego Repositorio del grupo de juego
     * @throws IOException Lanza excepción en caso de que falle la escritura o lectura de datos
     */
    private static void eliminarTodosLosGruposJuego(RepoGrupoJuego repoGrupoJuego) throws IOException {
        int opcion;
        System.out.println("Estas seguro de que quieres eliminar todos los grupos de juego, no hay marcha atrás");
        System.out.println("1.Sí");
        System.out.println("2.No");
        opcion = teclado.nextInt();
        if (opcion == 1)
            repoGrupoJuego.deleteAll();
    }

    /**
     * Función que comprueba si un grupo de juego existe
     *
     * @param repoGrupoJuego Repositorio del grupo de juego
     */
    private static void comprobarSiGrupoJuegoExiste(RepoGrupoJuego repoGrupoJuego) throws IOException {
        int idJugador;
        System.out.println("Introduce el id del grupo de juego cuya existencia quieras comprobar");
        idJugador = teclado.nextInt();
        if (repoGrupoJuego.existsById(idJugador))
            System.out.println("El grupo de juego con id " + idJugador + " sí existe");
        else
            System.out.println("El grupo de juego con id " + idJugador + " no existe");
    }

    /**
     * Función que busca un grupo de juego por ID
     *
     * @param repoGrupoJuego Repositorio del grupo de juego
     * @throws IllegalArgumentException Lanza una excepción en caso de que el id no sea válido
     */
    private static void buscarGrupoJuegoPorID(RepoGrupoJuego repoGrupoJuego) throws IllegalArgumentException, IOException {
        int idJugador;
        System.out.println("Introduce el id del grupo de juego que quieras buscar");
        idJugador = teclado.nextInt();
        System.out.println(repoGrupoJuego.findById(idJugador));
    }

    /**
     * Función que saca todos los grupos de juego a un Iterable
     *
     * @param repoGrupoJuego Repositorio de grupo de juego
     */
    private static void sacarGrupoJuegoIterable(RepoGrupoJuego repoGrupoJuego) throws IOException {
        Iterable<GrupoJuego> grupos = repoGrupoJuego.findAll();
        boolean vacio = true;
        for (GrupoJuego j : grupos) {
            System.out.println(j);
            vacio = false;
        }

        if (vacio)
            System.out.println("La lista está vacía");
    }

    /**
     * Guarda los grupos de juego creados en el fichero
     *
     * @param repoGrupoJuego Repositorio del grupo de juego
     * @throws IOException Lanza una excepción en caso de que ocurran problemas cuando se intenta guardar el grupo o grupos
     */
    private static void guardarGrupoJuego(RepoGrupoJuego repoGrupoJuego) throws IOException {
        if (grupoJugs.isEmpty()) {
            System.out.println("Aún no has creado ningún grupo de juego");
            return;
        }
        System.out.println("Vamos a intentar guardar todos los grupo de juego que has creado");
        for (GrupoJuego grupoJuego : grupoJugs) {
            try {
                repoGrupoJuego.save(grupoJuego);
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
        System.out.println("Se ha terminado el guardado");
        grupoJugs.clear();
    }

    /**
     * Función que busca los grupos de juego a los que pertenece un jugador
     *
     * @param repoGrupoJuego Repositorio de grupos de juego
     */
    private static void buscarGruposPorJugador(RepoGrupoJuego repoGrupoJuego) {
        int idJugador;
        System.out.println("Introduce el id del jugador que quieras buscar en los grupos: ");
        idJugador = teclado.nextInt();
        List<GrupoJuego> listaGruposJugador = repoGrupoJuego.buscarGruposPorIdJugador(idJugador);
        for (GrupoJuego grupo : listaGruposJugador) {
            System.out.println(grupo);
        }
        if (listaGruposJugador.isEmpty())
            System.out.println("No existe ningún grupo con ese jugador");

    }

}
