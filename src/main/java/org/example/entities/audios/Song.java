package org.example.entities.audios;

import java.util.Date;

public class Song extends Audio {
    private Album album;

    public Song(String name, int length, Date release, int id, Album album) {
        super(name, length, release, id);
        this.album = album;
    }
}
