package repository;

/**
 *
 * @author Grupo 3
 * @param <T>
 * @param <ID>
 */
public interface CrudRepository<T, ID> extends ReadRepository<T, ID>, WriteRepository<T, ID> {

}
