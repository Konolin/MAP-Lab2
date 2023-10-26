package org.example.entities.audios;

import org.example.entities.users.Artist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Album {
    private Integer id;
    private String name;
    private Artist artist;
    private List<Song> songs;
    private Date releaseDate;

    public Album(Integer id, String name, Artist artist, Date releaseDate) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.releaseDate = releaseDate;
        this.songs = new ArrayList<>();
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSong(Song song) {
        if (!songs.remove(song)) {
            throw new IllegalArgumentException("Album::Song can't be removed, it isn't part of the list.");
        }
    }
}
