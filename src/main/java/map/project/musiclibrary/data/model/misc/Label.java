package map.project.musiclibrary.data.model.misc;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import map.project.musiclibrary.data.model.users.ArtistUser;

import java.util.List;

@Data
@Entity
@Table(name = "labels")
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "label", fetch = FetchType.EAGER)
    @JsonBackReference
    private List<ArtistUser> artists;

    public boolean addArtist(ArtistUser artistUser) {
        return artists.add(artistUser);
    }

    @Override
    public String toString() {
        return "Label(" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", artists=" + ArtistUser.listToString(artists) +
                ')';
    }

    public String toShortString() {
        return "(ID: " + id +
                ", Name: " + name +
                ")";
    }
}
