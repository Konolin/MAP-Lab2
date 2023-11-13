package map.project.musiclibrary.data.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "songs")
@Data
public class Song extends Audio implements Playable {
    @Column(name = "genre")
    private String genre;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private ArtistUser artist;

    @ManyToOne
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

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

    @Override
    public void play() {
        System.out.printf("Playing song %s by %s", name, artist.getName());
    }
}
