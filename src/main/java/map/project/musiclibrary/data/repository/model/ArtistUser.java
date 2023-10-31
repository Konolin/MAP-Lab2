package map.project.musiclibrary.data.repository.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "artists")
@Data
public class ArtistUser extends User {
    @ManyToOne
    @JoinColumn(name = "label_id")
    private Label label;

    @OneToMany(mappedBy = "artist")
    private List<Album> albums;
}
