package repository;

import java.util.List;

/**
 *
 * @author Grupo 3
 * @param <T>
 * @param <ID>
 */
public interface ReadRepository<T, ID> {

    T getById(ID id) throws Exception;

    List<T> getAll() throws Exception;
}
