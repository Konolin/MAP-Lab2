package map.project.musiclibrary.data.model.audios;

import jakarta.persistence.*;
import lombok.Data;
import map.project.musiclibrary.data.model.audios.Song;
import map.project.musiclibrary.data.model.users.ArtistUser;

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

    @OneToMany(mappedBy = "album", fetch = FetchType.EAGER)
    private List<Song> songs;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "artist_id")
    private ArtistUser artist;

    public String toShortString() {
        return "Song(" +
                "id=" + id +
                ", name='" + name +
                "')";
    }

    @Override
    public String toString() {
        return "Album(" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", songs=" + Song.listToString(songs) +
                ')';
    }
}
