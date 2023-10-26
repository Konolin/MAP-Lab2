package org.example.entities.audios;

import java.util.Date;

public class Podcast extends Audio {
    private String host;

    public Podcast(String name, int length, Date release, int id, String host) {
        super(name, length, release, id);
        this.host = host;
    }
}
