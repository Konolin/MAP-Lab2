package org.example.entities.audios;

import org.example.entities.misc.Sponsor;
import org.example.entities.enums.PodcastGenre;
import org.example.entities.users.Artist;

import java.util.List;

public class Podcast extends Audio {
    private PodcastGenre genre;
    private List<Sponsor> sponsors;

    public Podcast(Integer id, String name, Artist artist, Integer duration, List<Artist> features, PodcastGenre genre, List<Sponsor> sponsors) {
        super(id, name, artist, duration, features);
        this.genre = genre;
        this.sponsors = sponsors;
    }
}
