package org.example.entities.audios;

import org.example.entities.users.User;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private Integer id;
    private User creator;
    private List<Song> songs;

    public Playlist(Integer id, User creator) {
        this.id = id;
        this.creator = creator;
        this.songs = new ArrayList<>();
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSong(Song song) {
        if (!songs.remove(song)) {
            throw new IllegalArgumentException("Playlist::Can't remove song, it isn't part of the playlist.");
        }
    }
}
