package org.example.entities.audios;

import org.example.entities.enums.MusicGenre;
import org.example.entities.users.Artist;

import java.util.List;

public class Song extends Audio {
    private Album album;
    private MusicGenre genre;

    public Song(Integer id, String name, Artist artist, Integer duration, List<Artist> features, Album album, MusicGenre genre) {
        super(id, name, artist, duration, features);
        this.album = album;
        this.genre = genre;
    }
}
