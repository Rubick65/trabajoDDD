package AgregadoAventura;

import AgregadoAventura.Repositorio.RepoAventura;

import java.io.IOException;

public class cargarAventuras {
    public static void main(String[] args) {
        try {
            RepoAventura repo = new RepoAventura();

            // Borramos antes de cargar nuevas aventuras
            repo.deleteAll();

            // Crear aventuras normales
            Aventura a1 = new Aventura("El Bosque Encantado", 5, Aventura.Dificultad.FACIL);
            Aventura a2 = new Aventura("Cueva Perdida", 8, Aventura.Dificultad.NORMAL);
            Aventura a3 = new Aventura("Castillo de Sombras", 10, Aventura.Dificultad.DIFICIL);

            // Crear aventuras de acción
            AventuraAccion aa1 = new AventuraAccion("Infiltración Secreta", 6, Aventura.Dificultad.NORMAL, 10, 5);

            // Crear aventuras de misterio
            AventuraMisterio am1 = new AventuraMisterio("Misterio en la Mansión", 4, Aventura.Dificultad.FACIL, "El retrato desaparecido");

            // Guardar en el repositorio
            repo.save(a1);
            repo.save(a2);
            repo.save(a3);
            repo.save(aa1);
            repo.save(am1);
            am1.setDificultad(Aventura.Dificultad.DIFICIL);
        } catch (Exception e) {
            System.err.println("Error cargando aventuras: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
