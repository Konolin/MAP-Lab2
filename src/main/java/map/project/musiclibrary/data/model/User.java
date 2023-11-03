package map.project.musiclibrary.data.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@MappedSuperclass
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    protected Long id;

    @Column(name = "name")
    protected String name;

    @Column(name = "email")
    protected String email;

    @Column(name = "birthdate")
    protected Date birthdate;

    public void setBirthdate(Date birthdate) {
        if (birthdate == null) {
            throw new IllegalArgumentException("Birthdate cannot be null");
        }
        this.birthdate = birthdate;
    }
}