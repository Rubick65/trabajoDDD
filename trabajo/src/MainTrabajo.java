import AgregadoAventura.mainAventuras;
import AgregadoGrupoJuego.mainGrupoJuego;
import AgregadoJugador.mainJugador;
import AgregadoPersonaje.mainPersonajes;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainTrabajo {
    static Scanner teclado = new Scanner(System.in);

    public static void main(String[] args) {
        mostrar_menu();
    }

    public static void mostrar_menu() {

        int opcion = 0;
        while (opcion != 5) {
            try {
                System.out.println("-----------------------------------");
                System.out.println("Menú agregados para salir introduzca 5");
                System.out.println("1.Agregado Jugador");
                System.out.println("2.Agregado Grupos de juego");
                System.out.println("3.Agregado Aventura");
                System.out.println("4.Agregado Personaje");
                System.out.println("5.Salir");
                System.out.println("-----------------------------------");

                opcion = teclado.nextInt();

                lanzar_menus(opcion);

            } catch (InputMismatchException e) {
                teclado.nextLine();
                System.err.println("Introduce solo números del 1-5");
            }

        }
    }

    private static void lanzar_menus(int opcion) {
        switch (opcion) {
            case 1 -> mainJugador.main(new String[0]);
            case 2 -> mainGrupoJuego.main(new String[0]);
            case 3 -> mainAventuras.main(new String[0]);
            case 4 -> mainPersonajes.main(new String[0]);
        }

    }
}
