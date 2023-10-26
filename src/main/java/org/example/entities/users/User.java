package org.example.entities.users;

import java.util.Date;

public abstract class User {
    private Integer id;
    private String name;
    private String email;
    private Date birthDate;

    public User(Integer id, String name, String email, Date birthDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
    }
}
