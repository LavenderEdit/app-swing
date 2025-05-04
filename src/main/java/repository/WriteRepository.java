package repository;

/**
 *
 * @author Grupo 3
 * @param <T>
 * @param <ID>
 */
public interface WriteRepository<T, ID> {
    void create(T entity) throws Exception;
    void update(T entity) throws Exception;
    void delete(ID id) throws Exception;
}
