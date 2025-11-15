package AgregadoGrupoJuego;

import AgregadoGrupoJuego.Repositorio.RepoGrupoJuego;
import AgregadoJugador.Repositorio.RepoJugador;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class cargarGrupos {
    public static void main(String[] args) throws IOException {
        RepoGrupoJuego repoGrupo = new RepoGrupoJuego();
        RepoJugador repoJugador = new RepoJugador();

        List<GrupoJuego> grupos = Arrays.asList(
                new GrupoJuego("Los Valientes del Norte", "Grupo especializado en exploraciones árticas", List.of(1, 2, 3)),
                new GrupoJuego("Orden de la Llama", "Paladines y magos de fuego", List.of(2, 4, 5)),
                new GrupoJuego("Sombra Carmesí", "Asesinos silenciosos", List.of(6, 7)),
                new GrupoJuego("Los Buscadores de Gloria", "Aventureros que buscan fama", List.of(1, 5)),
                new GrupoJuego("Guardia del Alba", "Defensores del reino", List.of(3, 4, 8)),
                new GrupoJuego("Clan Rocanegra", "Guerreros enanos de las montañas", List.of(9, 10, 11)),
                new GrupoJuego("Círculo Druídico del Roble", "Druidas y guardianes de la naturaleza", List.of(12, 2))
        );

        for (GrupoJuego g : grupos) {
            repoGrupo.save(g);
            System.out.println("Guardado: " + g.getNombreGrupo());
        }

    }
}
