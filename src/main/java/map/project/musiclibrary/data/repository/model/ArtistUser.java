package map.project.musiclibrary.data.repository.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "artists")
public class ArtistUser extends User {
    @ManyToOne
    @JoinColumn(name = "label_id")
    private Label label;

    @OneToMany(mappedBy = "artist")
    private List<Album> albums;
}
