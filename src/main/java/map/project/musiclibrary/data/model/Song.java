package map.project.musiclibrary.data.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "songs")
@Data
public class Song extends Audio {
    @Column(name = "genre")
    private String genre;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private ArtistUser artist;

    @Override
    public String toString() {
        return "Song(" +
                "id=" + id +
                ", genre='" + genre + '\'' +
                ", album=" + album +
                ", name='" + name + '\'' +
                ", length=" + length +
                ", releaseDate=" + releaseDate +
                ')';
    }
}
