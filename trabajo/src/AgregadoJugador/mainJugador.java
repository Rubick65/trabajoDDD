package AgregadoJugador;

import AgregadoGrupoJuego.GrupoJuego;
import AgregadoGrupoJuego.Repositorio.RepoGrupoJuego;
import AgregadoJugador.Repositorio.RepoJugador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class mainJugador {
    static Scanner teclado = new Scanner(System.in);
    static List<Jugador> jugs = new ArrayList<>();

    public static void main(String[] args) {
        jugs.add(new Jugador("02790162D", "Rubén", new DireccionJuego("Madrid", "Calle del besito 32", "1C", "28017")));
        try {
            RepoJugador repoJugador = new RepoJugador();
            RepoGrupoJuego repoGrupoJuego = new RepoGrupoJuego();
            menuPrincipal(repoJugador, repoGrupoJuego);
        } catch (IOException e) {
            System.err.println("Fallo a la hora de leer el archivo " + e.getMessage());
        }

    }

    private static void menuPrincipal(RepoJugador repoJugador, RepoGrupoJuego repoGrupoJuego) {
        int opcionJugador = 0;
        while (opcionJugador != 13) {
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
                System.out.println("12.Actualizar un jugadores");
                System.out.println("-------------------------------------------------------");

                opcionJugador = teclado.nextInt();
                opcionesPrincipales(opcionJugador, repoJugador, repoGrupoJuego);

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

    private static void opcionesPrincipales(int opcionJugador, RepoJugador repoJugador, RepoGrupoJuego repoGrupoJuego) throws IllegalArgumentException, IOException {
        switch (opcionJugador) {
            case 1:
                menuCreacionJugador();
                break;
            case 2:
                buscarPorIDOpcional(repoJugador);
                break;
            case 3:
                System.out.println(repoJugador.findAllToList());
                break;
            case 4:
                System.out.println("La cantidad de jugadores es: " + repoJugador.count());
                break;
            case 5:
                eliminarJugadorPorId(repoJugador, repoGrupoJuego);
                break;
            case 6:
                eliminarTodosLosJugadores(repoJugador, repoGrupoJuego);
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
            case 12:
                repoJugador.actualizarDatos();
                System.out.println("Datos actualizados");
                break;
            default:
                break;
        }


    }

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

    private static void crearJugador(DireccionJuego direccionJuego) {
        String dni, nombre;
        teclado.nextLine();
        System.out.println("Introduce el nombre del jugador:");
        nombre = teclado.nextLine();
        System.out.println("Introduce el DNI del jugador:");
        dni = teclado.next();
        jugs.add(new Jugador(dni, nombre, direccionJuego));
    }

    private static void creaDirectorJuego(DireccionJuego direccionJuego) throws IllegalArgumentException {
        String dni, nombre;
        System.out.println("Vamos a crear un jugador");

        System.out.println("Introduce el nombre del jugador:");
        nombre = teclado.nextLine();
        System.out.println("Introduce el DNI del jugador:");
        dni = teclado.next();
        jugs.add(new DirectorDeJuego(dni, nombre, direccionJuego));
        teclado.nextLine();

    }

    private static void buscarPorIDOpcional(RepoJugador repoJugador) throws IllegalArgumentException {
        int idJugador;
        System.out.println("Introduce el id del jugador que quieras buscar:");
        idJugador = teclado.nextInt();
        System.out.println(repoJugador.findByIdOptional(idJugador));

    }

    private static void eliminarJugadorPorId(RepoJugador repoJugador, RepoGrupoJuego repoGrupoJuego) throws IOException, IllegalArgumentException {
        int idJugador;
        System.out.println("Introduce el id del jugador que quieras eliminar:");
        idJugador = teclado.nextInt();
        repoJugador.deleteById(idJugador);
        System.out.println("El jugador ha sido eliminado con éxito");
        System.out.println("Ahora voy a eliminar todas las referencias a este jugador en los grupos: ");
        eliminarJugadoresDeGrupos(idJugador, repoGrupoJuego);

    }

    private static void eliminarJugadoresDeGrupos(int idJugador, RepoGrupoJuego repoGrupoJuego) {
        List<GrupoJuego> listaGrupos = repoGrupoJuego.findAllToList();

        listaGrupos.forEach(grupo -> {
            if (grupo.getListaMiembros().contains(idJugador)) {
                try {
                    grupo.eliminarJugador(idJugador);

                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }
        });
    }

    private static void eliminarTodosLosJugadores(RepoJugador repoJugador, RepoGrupoJuego repoGrupoJuego) throws IOException {
        int opcion;
        System.out.println("Estas seguro de que quieres eliminar todos los jugadores, no hay marcha atrás, también se eliminarán todos los grupos de juego");
        System.out.println("1.Sí");
        System.out.println("2.No");
        opcion = teclado.nextInt();
        if (opcion == 1) {
            repoJugador.deleteAll();
            repoGrupoJuego.deleteAll();
        }


    }

    private static void comprobarSiJugadorExiste(RepoJugador repoJugador) {
        int idJugador;
        System.out.println("Introduce el id del jugador cuya existencia quieras comprobar");
        idJugador = teclado.nextInt();
        if (repoJugador.existsById(idJugador))
            System.out.println("El jugador con id " + idJugador + " sí existe");
        else
            System.out.println("El Jugador con id " + idJugador + " no existe");
    }

    private static void buscarJugadorPorID(RepoJugador repoJugador) throws IllegalArgumentException {
        int idJugador;
        System.out.println("Introduce el id del jugador que quieras buscar");
        idJugador = teclado.nextInt();
        System.out.println("Jugador: " + repoJugador.findById(idJugador));
    }

    private static void sacarJugadoresIterable(RepoJugador repoJugador) {
        for (Jugador j : repoJugador.findAll()) {
            System.out.println(j);
        }

    }

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

    private static void buscarJugadoresPorCalle(RepoJugador repoJugador) {
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

