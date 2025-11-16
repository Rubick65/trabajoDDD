package AgregadoAventura.Repositorio;

import AgregadoAventura.Aventura;
import Interfaces.IRepositorioExtend;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RepoAventura implements IRepositorioExtend<Aventura, Integer> {

    private final File archivo = new File("Aventuras.json");
    private final ObjectMapper oM = new ObjectMapper();
    private final ObjectWriter writer = oM.writerWithDefaultPrettyPrinter();
    private static int contadorID;
    private Map<Integer, Aventura> listaAventuras;


    public RepoAventura() throws IOException {
        recibirDatosFichero();
    }

    public List<Aventura> buscarAventuraPorDificultad(Aventura.Dificultad dificultad) {
        return listaAventuras.values().stream().filter(aventura -> aventura.getDificultad().equals(dificultad)).toList();
    }

    @Override
    public Optional<Aventura> findByIdOptional(Integer id) {
        return Optional.ofNullable(listaAventuras.get(id));
    }

    @Override
    public List<Aventura> findAllToList() {
        return List.copyOf(listaAventuras.values());
    }

    @Override
    public long count() throws IOException {
        return listaAventuras.size();
    }

    @Override
    public void deleteById(Integer id) throws IOException {
        recibirDatosFichero();
        comprobarExistenciaClave(id);
        listaAventuras.remove(id);
        guardarDatos();
    }

    @Override
    public void deleteAll() throws IOException {
        contadorID = 0;
        listaAventuras = new HashMap<>();
        guardarDatos();
    }

    @Override
    public boolean existsById(Integer id) {
        return listaAventuras.containsKey(id);
    }

    @Override
    public Aventura findById(Integer id) throws IOException {
        comprobarExistenciaClave(id);
        return listaAventuras.get(id);
    }

    @Override
    public Iterable<Aventura> findAll() {
        return listaAventuras.values();
    }

    @Override
    public <S extends Aventura> S save(S entity) throws Exception {
        recibirDatosFichero();
        if (listaAventuras.containsValue(entity))
            throw new IllegalArgumentException("La aventura ya existe en el archivo");

        entity.setID_AVENTURA(contadorID);
        listaAventuras.put(entity.getID_AVENTURA(), entity);
        guardarDatos();
        return entity;
    }

    /**
     * Actualiza la entidad si ya existía antes
     *
     * @param entity Entidad a actualizar
     * @param <S>    Entidad he hijos
     * @return Devuelve la entidad actualizada
     * @throws IOException Lanza excepción en caso de problemas a la hora de la escritura
     */
    public <S extends Aventura> S actualizarDatos(S entity) throws IOException {
        comprobarExistenciaClave(entity.getID_AVENTURA());
        listaAventuras.put(entity.getID_AVENTURA(), entity);
        guardarDatos();
        return entity;
    }

    private void guardarDatos() throws IOException {
        Map<Integer, JsonNode> jsonMap = new HashMap<>();
        for (Map.Entry<Integer, Aventura> entry : listaAventuras.entrySet()) {
            jsonMap.put(entry.getKey(), oM.valueToTree(entry.getValue()));
        }
        oM.writerWithDefaultPrettyPrinter().writeValue(archivo, jsonMap);
    }

    private void recibirDatosFichero() throws IOException {
        if (archivo.exists() && archivo.length() > 0) {
            listaAventuras = oM.readValue(archivo, new TypeReference<Map<Integer, Aventura>>() {
            });
            /*
             Saca todos los ids del Hash Map y los compara en caso de que la lista este vacía se pondrá por defecto 1
             en caso contrario se pondrá último id + 1
             */
            contadorID = listaAventuras.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
        } else {
            listaAventuras = new HashMap<>();
        }
    }

    private void comprobarExistenciaClave(Integer o) {
        if (!existsById(o))
            throw new IllegalArgumentException("En la lista no existe ninguna aventura con este id");
    }
}

