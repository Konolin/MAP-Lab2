package map.project.musiclibrary.data.repository.model;

import jakarta.persistence.*;

@Entity
@Table(name = "songs")
public class Song extends Audio {
    @Column(name = "genre")
    private String genre;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;
}
