package org.example.entities.actions;

import org.example.entities.audios.Song;
import org.example.entities.users.User;

public class Review {
    private Integer id;
    private User user;
    private Song song;
    private Integer rating; // should be limited to 1-10
    private String reason;

    public Review(Integer id, User user, Song song, Integer rating, String reason) {
        this.id = id;
        this.user = user;
        this.song = song;
        this.rating = rating;
        this.reason = reason;
    }
}
