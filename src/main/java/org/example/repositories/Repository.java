package org.example.repositories;

import java.util.List;

public interface Repository<T, ID> {
    List<T> getAll();

    T getById(ID id);

    boolean addEntity(T entity);

    boolean removeById(ID id);

    boolean removeEntity(T entity);

    boolean update(ID id, T newEntity);
}
