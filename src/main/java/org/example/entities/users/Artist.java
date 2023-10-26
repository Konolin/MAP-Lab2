package org.example.entities.users;

import org.example.entities.Label;
import org.example.entities.audios.Album;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Artist extends User {
    private Label label;
    private List<Album> albums;

    public Artist(Integer id, String name, String email, Date birthDate, Label label) {
        super(id, name, email, birthDate);
        this.label = label;
        this.albums = new ArrayList<>();
    }

    public void addAlbum(Album album) {
        albums.add(album);
    }

    public void remove(Album album) {
        if (!albums.remove(album)) {
            throw new IllegalArgumentException("Artist::This album can't be removed, it isn't part of the list");
        }
    }
}
