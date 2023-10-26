package org.example.entities.audios;

import java.util.Date;

public abstract class Audio {
    private String name;
    private int length; // in seconds
    private Date release;
    private int id;

    public Audio(String name, int length, Date release, int id) {
        this.name = name;
        this.length = length;
        this.release = release;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Date getRelease() {
        return release;
    }

    public void setRelease(Date release) {
        this.release = release;
    }
}
