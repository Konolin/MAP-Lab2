package map.project.musiclibrary.data.model.audios;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import map.project.musiclibrary.data.model.strategies.Playable;
import map.project.musiclibrary.data.model.users.ArtistUser;

import java.util.List;

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

    @ManyToMany(mappedBy = "songs")
    private List<Playlist> playlists;

    public static String listToString(List<Song> songs) {
        String songsString = "[]";
        if (songs != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (Song song : songs) {
                sb.append(song.toShortString()).append(", ");
            }
            if (!songs.isEmpty()) {
                sb.delete(sb.length() - 2, sb.length());
            }
            sb.append("]");
            songsString = sb.toString();
        }
        return songsString;
    }

    @Override
    public String toString() {
        String albumStr = album != null ? album.toShortString() : "null";
        return "Song(" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", album=" + albumStr +
                ", artist=" + artist.toShortString() +
                ", genre='" + genre + '\'' +
                ", length=" + length +
                ", releaseDate=" + releaseDate +
                ')';
    }

    public String toShortString() {
        return "Song(" +
                "id=" + id +
                ", name='" + name +
                ')';
    }

    public String play(Playable playableStrategy) {
        return playableStrategy.play(name, artist.getName());
    }
}
