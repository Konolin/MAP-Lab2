package map.project.musiclibrary.data.repository.model;

import jakarta.persistence.*;

import java.util.Date;

@MappedSuperclass
public abstract class Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "artist")
    private String artist;

    @Column(name = "releaseDate")
    private Date releaseDate;
}
