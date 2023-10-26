package org.example.entities.users;

import java.util.Date;

public class User {
    private String name;
    private String email;
    private Date birthDate;
    private int id;

    public User(String name, String email, Date birthDate, int id) {
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
