package org.example.repository;

import java.util.List;

public interface Repository<T, ID> {
    List<T> findAll();

    T findById(ID id);

    void delete(T entity);

    void deleteById(ID id);

    void addEntity(T entity);
}
