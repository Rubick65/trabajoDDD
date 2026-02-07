package AgregadoPersonaje;

import AgregadoPersonaje.Repositorio.RepoPersonaje;

import java.io.IOException;
import java.util.ArrayList;

public class ServicioPersonaje {

    private RepoPersonaje repo = null;

    public ServicioPersonaje() throws IOException {
        repo = new RepoPersonaje();
    }

    public void aniadirObjetoAPersonaje(int idPersonaje, ObjetoInventario objeto) throws IOException {
        Personaje p = repo.findById(idPersonaje);
        p.agregarObjeto(objeto);
        repo.save(p);
    }
}
