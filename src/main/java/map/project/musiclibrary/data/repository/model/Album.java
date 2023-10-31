package map.project.musiclibrary.data.repository.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "albums")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private ArtistUser artist;

    @OneToMany(mappedBy = "album")
    private List<Song> songs;
}
