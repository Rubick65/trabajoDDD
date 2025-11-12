package Interfaces;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IRepositorioExtend<T, ID> extends
        IRepositorio<T, ID> {

    /**
     * Devuelve la entidad T con identificador id.
     *
     * @param id Identificador de la entidad
     * @return Entidad que tiene como identificador id u
     * Optional#empty() si no se encuentra
     * @throws IllegalArgumentException En caso de ser id nulo
     */
    Optional<T> findByIdOptional(ID id) throws IOException;

    /**
     * Devuelve todas las instancias de tipo T
     *
     * @return todas las instancias.
     *
     */
    List<T> findAllToList();

}
