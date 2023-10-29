package map.project.musiclibrary.data.repository.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@MappedSuperclass
public abstract class Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    protected Long id;

    @Column(name = "name")
    protected String name;

    @Column(name = "length")
    protected Integer length;

    @Column(name = "releaseDate")
    protected Date releaseDate;

    public void play() {
        System.out.println(name + " is now playing...");
    }
}
