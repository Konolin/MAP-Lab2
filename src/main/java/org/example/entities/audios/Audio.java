package org.example.entities.audios;

import org.example.entities.users.Artist;

import java.util.List;

public abstract class Audio {
    private Integer id;
    private String name;
    private Artist artist;
    private Integer duration; // in seconds
    private List<Artist> features;

    public Audio(Integer id, String name, Artist artist, Integer duration, List<Artist> features) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.duration = duration;
        this.features = features;
    }
}
