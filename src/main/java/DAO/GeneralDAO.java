package DAO;

public interface GeneralDAO<T> {
    boolean add(T object);

    boolean update(T object);

    boolean delete(T object);

}
