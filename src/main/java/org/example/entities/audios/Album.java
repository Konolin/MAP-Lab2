package org.example.entities.audios;

import org.example.entities.users.Artist;

import java.util.ArrayList;
import java.util.List;

public class Album {
    private List<Song> songs;
    private String name;
    private Artist artist;

    public Album(String name, Artist artist) {
        this.songs = new ArrayList<>();
        this.name = name;
        this.artist = artist;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "Album{" +
                "songs=" + songs +
                ", name='" + name + '\'' +
                ", artist=" + artist +
                '}';
    }

    public void addSong(Song song) {
        songs.add(song);
    }
}
