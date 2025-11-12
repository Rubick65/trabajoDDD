package AgregadoGrupoJuego;

import java.util.ArrayList;
import java.util.List;

public class mainPruebaRubenGrupo {
    public static void main(String[] args) {

        try {
            List<Integer> listaMiembros = new ArrayList<>();
            GrupoJuego grupoJuego = new GrupoJuego("Los capis", "Este grupo es sobre capis", listaMiembros);

        }catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }

    }
}
