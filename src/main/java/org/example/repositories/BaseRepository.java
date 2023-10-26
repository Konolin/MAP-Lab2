package org.example.repositories;

import java.util.List;

public abstract class BaseRepository<T, ID> implements Repository<T, ID> {
    @Override
    public List<T> getAll() {
        return null;
    }

    @Override
    public T getById(ID id) {
        return null;
    }

    @Override
    public boolean addEntity(T entity) {
        return false;
    }

    @Override
    public boolean removeById(ID id) {
        return false;
    }

    @Override
    public boolean removeEntity(T entity) {
        return false;
    }

    @Override
    public boolean update(ID id, T newEntity) {
        return false;
    }
}
