package AgregadoJugador;

import AgregadoGrupoJuego.GrupoJuego;
import AgregadoGrupoJuego.Repositorio.RepoGrupoJuego;
import AgregadoJugador.Repositorio.RepoJugador;
import AgregadoPersonaje.Personaje;
import AgregadoPersonaje.Repositorio.RepoPersonaje;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class mainJugador {
    static Scanner teclado = new Scanner(System.in);
    static List<Jugador> jugs = new ArrayList<>();

    public static void main(String[] args) {
        try {
            RepoJugador repoJugador = new RepoJugador();
            RepoGrupoJuego repoGrupoJuego = new RepoGrupoJuego();
            RepoPersonaje repoPersonaje = new RepoPersonaje();

            menuPrincipal(repoJugador, repoGrupoJuego, repoPersonaje);
        } catch (IOException e) {
            System.err.println("Fallo a la hora de leer el archivo " + e.getMessage());
        }

    }

    /**
     * Función que muestra el menú principal con las opciones del respositorio
     *
     * @param repoJugador    Repositorio con los jugadores
     * @param repoGrupoJuego Repositorio con los grupos de juego
     * @param repoPersonaje  Repositorio con los personajes
     */
    private static void menuPrincipal(RepoJugador repoJugador, RepoGrupoJuego repoGrupoJuego, RepoPersonaje repoPersonaje) {
        int opcionJugador = 0;
        while (opcionJugador != 12) {
            try {
                System.out.println("-------------------------------------------------------");
                System.out.println("Menú, para salir introduce 12");
                System.out.println("-------------------------------------------------------");
                System.out.println("1.Crear Jugador");
                System.out.println("2.Buscar jugador por id opcional");
                System.out.println("3.Sacar jugadores a lista");
                System.out.println("4.Contar jugadores");
                System.out.println("5.Eliminar jugador");
                System.out.println("6.Eliminar todos los jugadores");
                System.out.println("7.Comprobar si un jugador existe");
                System.out.println("8.Buscar jugador por id");
                System.out.println("9.Sacar todos los jugadores a un Iterable");
                System.out.println("10.Guardar jugador");
                System.out.println("11.Buscar jugadores por calle");
                System.out.println("12.Salir");
                System.out.println("-------------------------------------------------------");

                opcionJugador = teclado.nextInt();
                opcionesPrincipales(opcionJugador, repoJugador, repoGrupoJuego, repoPersonaje);

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
     * Función que indica que método del grupo de juego se debe ejecutar
     *
     * @param opcionJugador  Opción seleccionada por el jugador
     * @param repoJugador    Repositorio con los jugadores
     * @param repoGrupoJuego Repositorio con los grupos de juego
     * @param repoPersonaje  Repositorio con los personajes
     * @throws IllegalArgumentException Lanza la excepción si se intenta poner algún dato incorrecto
     * @throws IOException              Lanza una excepción en caso de que ocurra algún problema a la hora de leer o escribir en el fichero
     */
    private static void opcionesPrincipales(int opcionJugador, RepoJugador repoJugador, RepoGrupoJuego repoGrupoJuego, RepoPersonaje repoPersonaje) throws IllegalArgumentException, IOException {
        switch (opcionJugador) {
            case 1:
                menuCreacionJugador();
                break;
            case 2:
                buscarPorIDOpcional(repoJugador);
                break;
            case 3:
                List<Jugador> listaJugadores = repoJugador.findAllToList();
                for (Jugador jugador : listaJugadores) {
                    System.out.println(jugador);
                }
                if (listaJugadores.isEmpty())
                    System.out.println("La lista está vacía");
                break;
            case 4:
                System.out.println("La cantidad de jugadores es: " + repoJugador.count());
                break;
            case 5:
                eliminarJugadorPorId(repoJugador, repoGrupoJuego, repoPersonaje);
                break;
            case 6:
                eliminarTodosLosJugadores(repoJugador, repoGrupoJuego, repoPersonaje);
                break;
            case 7:
                comprobarSiJugadorExiste(repoJugador);
                break;
            case 8:
                buscarJugadorPorID(repoJugador);
                break;
            case 9:
                sacarJugadoresIterable(repoJugador);
                break;
            case 10:
                guardarJugador(repoJugador);
                break;
            case 11:
                buscarJugadoresPorCalle(repoJugador);
                break;
            default:
                break;
        }
    }

    /**
     * Función que crea el menú de creación de jugador
     */
    private static void menuCreacionJugador() {
        int tipoJugador = 0;
        while (tipoJugador != 3) {
            try {

                System.out.println("Que tipo de jugador quieres crear? Para salir introduce 3");
                System.out.println("1.Jugador Normal");
                System.out.println("2.Jugador Director de juego");
                tipoJugador = teclado.nextInt();

                switch (tipoJugador) {
                    case 1:
                        DireccionJuego direccionJuego1 = crearDireccionJuego();
                        crearJugador(direccionJuego1);
                        break;
                    case 2:
                        DireccionJuego direccionJuego2 = crearDireccionJuego();
                        creaDirectorJuego(direccionJuego2);
                        break;
                }
            } catch (InputMismatchException e) {
                System.err.println("Introduce solo números " + e.getMessage());
                teclado.nextLine();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Función que sirve para crear una dirección de juego
     *
     * @return Devuleve una dirección de juego
     */
    private static DireccionJuego crearDireccionJuego() {
        String ciudad, calle, piso, codigoPostal;
        DireccionJuego direccionDeJuego;
        System.out.println("Primero vamos a crear la dirección de juego: ");
        while (true) {
            try {
                teclado.nextLine();
                System.out.println("Introduce la ciudad:");
                ciudad = teclado.nextLine();
                System.out.println("Introduce la calle:");
                calle = teclado.nextLine();
                System.out.println("Introduce el piso:");
                piso = teclado.next();
                System.out.println("Introduce el código postal");
                codigoPostal = teclado.next();
                direccionDeJuego = new DireccionJuego(ciudad, calle, piso, codigoPostal);
                return direccionDeJuego;

            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Función que sirve para crear un jugador
     *
     * @param direccionJuego Dirección de juego
     */
    private static void crearJugador(DireccionJuego direccionJuego) {
        String dni, nombre;
        teclado.nextLine();
        System.out.println("Introduce el nombre del jugador:");
        nombre = teclado.nextLine();
        System.out.println("Introduce el DNI del jugador:");
        dni = teclado.next();
        jugs.add(new Jugador(dni, nombre, direccionJuego));
    }

    /**
     * Función que sirve para crear un director de juego
     *
     * @param direccionJuego Dirección de juego
     * @throws IllegalArgumentException Lanza una excepción en caso de que se ponga datos no válidos
     */
    private static void creaDirectorJuego(DireccionJuego direccionJuego) throws IllegalArgumentException {
        String dni, nombre;
        System.out.println("Vamos a crear un jugador");
        teclado.nextLine();
        System.out.println("Introduce el nombre del jugador:");
        nombre = teclado.nextLine();
        System.out.println("Introduce el DNI del jugador:");
        dni = teclado.next();
        jugs.add(new DirectorDeJuego(dni, nombre, direccionJuego));
        teclado.nextLine();
    }

    /**
     * Función que busca un jugador por id
     *
     * @param repoJugador Repositorio del jugador
     * @throws IllegalArgumentException Lanza una excepción en caso de tener un id no válido
     */
    private static void buscarPorIDOpcional(RepoJugador repoJugador) throws IllegalArgumentException, IOException {
        int idJugador;
        System.out.println("Introduce el id del jugador que quieras buscar:");
        idJugador = teclado.nextInt();
        System.out.println(repoJugador.findByIdOptional(idJugador));

    }

    /**
     * Función que elimina un jugador por id, asi como eliminar las referencias a ese jugador de todas las otras clases
     * relacionadas
     *
     * @param repoJugador    Repositorio con los jugadores
     * @param repoGrupoJuego Repositorio con los grupos de juego
     * @param repoPersonaje  Repositorio con los personajes
     * @throws IOException              Lanza una excepción en caso de problemas con la lectura o escritura del archivo
     * @throws IllegalArgumentException Lanza una excepción en caso de introducir un id no válido
     */
    private static void eliminarJugadorPorId(RepoJugador repoJugador, RepoGrupoJuego repoGrupoJuego, RepoPersonaje repoPersonaje) throws IOException, IllegalArgumentException {
        int idJugador;
        System.out.println("Introduce el id del jugador que quieras eliminar:");
        idJugador = teclado.nextInt();
        repoJugador.deleteById(idJugador);
        System.out.println("El jugador ha sido eliminado con éxito");
        System.out.println("Ahora voy a eliminar todas las referencias a este jugador en los grupos y personajes: ");
        eliminarJugadoresDeGrupos(idJugador, repoGrupoJuego);
        eliminarJugadorPersonaje(idJugador, repoPersonaje);

    }

    /**
     * Función que elimina al jugador pasado como parámetro de todos los grupos de juego en los que se encuentre
     *
     * @param idJugador      ID del jugador a eliminar
     * @param repoGrupoJuego Repositorio de los grupos de juego
     */
    private static void eliminarJugadoresDeGrupos(int idJugador, RepoGrupoJuego repoGrupoJuego) throws IOException {
        List<GrupoJuego> listaGrupos = repoGrupoJuego.findAllToList();

        listaGrupos.forEach(grupo -> {
            if (grupo.getListaMiembros().contains(idJugador)) {
                try {
                    grupo.eliminarJugador(idJugador);
                    repoGrupoJuego.actualizarDatos(grupo);
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Función que elimina la referencia del jugador en todos los personajes que tenga
     *
     * @param idJugador     Id del jugador a eliminar
     * @param repoPersonaje Repsoitorio de jugadores
     */
    private static void eliminarJugadorPersonaje(int idJugador, RepoPersonaje repoPersonaje) throws IOException {
        List<Personaje> listaGrupos = repoPersonaje.findAllToList();

        listaGrupos.forEach(personaje -> {
            if (personaje.getID_JUGADOR() == idJugador) {
                try {
                    personaje.setID_JUGADOR(0);
                    repoPersonaje.actualizarDatos(personaje);
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Elimina todos los jugadores del fichero asi cómo todos los grupos y todas las referencias en personajes
     *
     * @param repoJugador    Repositorio con los jugadores
     * @param repoGrupoJuego Repositorio con los grupos de juego
     * @param repoPersonaje  Repositorio con los personajes
     * @throws IOException Lanza una excepción en caso de que ocurra algún problema a la hora de leer o escribir en el archivo
     */
    private static void eliminarTodosLosJugadores(RepoJugador repoJugador, RepoGrupoJuego repoGrupoJuego, RepoPersonaje repoPersonaje) throws IOException {
        int opcion;
        System.out.println("Estas seguro de que quieres eliminar todos los jugadores, no hay marcha atrás, también se eliminarán todos los grupos de juego");
        System.out.println("1.Sí");
        System.out.println("2.No");
        opcion = teclado.nextInt();
        if (opcion == 1) {
            repoJugador.deleteAll();
            repoGrupoJuego.deleteAll();
            eliminarReferenciasEnPersonaje(repoPersonaje);
        }
    }

    /**
     * Función que eliminar las referencias a los jugadores en los personajes
     *
     * @param repoPersonaje Repositorio de los personajes
     */
    private static void eliminarReferenciasEnPersonaje(RepoPersonaje repoPersonaje) throws IOException {
        List<Personaje> personajes = repoPersonaje.findAllToList();
        for (Personaje personaje : personajes) {
            personaje.setID_PERSONAJE(0);
        }

    }

    /**
     * Función que comprueba si un jugador existe
     *
     * @param repoJugador Repositorio del jugador
     */
    private static void comprobarSiJugadorExiste(RepoJugador repoJugador) throws IOException {
        int idJugador;
        System.out.println("Introduce el id del jugador cuya existencia quieras comprobar");
        idJugador = teclado.nextInt();
        if (repoJugador.existsById(idJugador))
            System.out.println("El jugador con id " + idJugador + " sí existe");
        else
            System.out.println("El Jugador con id " + idJugador + " no existe");
    }

    /**
     * Función que busca un jugador por un id
     *
     * @param repoJugador Respositorio del jugador
     * @throws IllegalArgumentException Lanza una excepción en caso de que el id no sea válido
     */
    private static void buscarJugadorPorID(RepoJugador repoJugador) throws IllegalArgumentException, IOException {
        int idJugador;
        System.out.println("Introduce el id del jugador que quieras buscar");
        idJugador = teclado.nextInt();
        System.out.println("Jugador: " + repoJugador.findById(idJugador));
    }

    /**
     * Función que permite sacar todos los jugadores a un Iterable
     *
     * @param repoJugador Respositorio de jugadores
     */
    private static void sacarJugadoresIterable(RepoJugador repoJugador) throws IOException {
        Iterable<Jugador> jugadores = repoJugador.findAll();
        boolean vacio = true;
        for (Jugador j : jugadores) {
            System.out.println(j);
            vacio = false;
        }

        if (vacio)
            System.out.println("La lista está vacía");


    }

    /**
     * Función que permite guardar los jugadores creados
     *
     * @param repoJugador Respositorio con los jugadores
     * @throws IOException Lanza excepción en caso de que ocurra un error en la lectura o escritura de los archivos
     */
    private static void guardarJugador(RepoJugador repoJugador) throws IOException {
        if (jugs.isEmpty()) {
            System.out.println("Aún no has creado ningún jugador");
            return;
        }
        System.out.println("Vamos a intentar guardar todos los jugadores que has creado");
        for (Jugador jugador : jugs) {
            try {
                repoJugador.save(jugador);
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }


            System.out.println("Se ha terminado el guardado");
        }
        jugs.clear();
    }

    /**
     * Función que permite buscar jugadores por la calle en la que juegan
     *
     * @param repoJugador Repositorio de jugadores
     */
    private static void buscarJugadoresPorCalle(RepoJugador repoJugador) throws IOException {
        String calle;
        List<Jugador> jugadores;
        teclado.nextLine();
        System.out.println("Introduce la calle de los jugadores que quieras buscar:");
        calle = teclado.nextLine();
        jugadores = repoJugador.buscarJugadorPorDireccion(calle);
        if (jugadores.isEmpty()) {
            System.out.println("No existen jugadores con esa dirección de juego");
            return;
        }
        for (Jugador jugador : jugadores) {
            System.out.println(jugador);
        }
    }

}

