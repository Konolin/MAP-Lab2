package org.example.repository;

import org.example.entities.audios.Song;

import java.util.List;

public class InMemoryRepository implements Repository<Song, Integer> {
    @Override
    public List<Song> findAll() {
        return null;
    }

    @Override
    public Song findById(Integer integer) {
        return null;
    }

    @Override
    public void delete(Song entity) {

    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void addEntity(Song entity) {

    }
}
