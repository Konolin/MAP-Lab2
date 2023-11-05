package map.project.musiclibrary.data.model;

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

    @OneToMany(mappedBy = "artist", fetch = FetchType.EAGER)
    private List<Song> songs;

    public boolean addSong(Song song) {
        return songs.add(song);
    }

    @Override
    public String toString() {
        String labelStr = label == null ? "null" : label.toString();
        return "ArtistUser(" +
                "id=" + id +
                ", label=" + labelStr +
                ", songs=" + Song.listToString(songs) +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthdate=" + birthdate +
                ')';
    }

    public String toShortString() {
        return "(ID: " + id +
                ", Name: " + name +
                ")";
    }

    public static String listToString(List<ArtistUser> artists) {
        String artistsString = "[]";
        if (artists != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (ArtistUser artist : artists) {
                sb.append(artist.toShortString()).append(", ");
            }
            if (!artists.isEmpty()) {
                sb.delete(sb.length() - 2, sb.length());
            }
            sb.append("])");
            artistsString = sb.toString();
        }
        return artistsString;
    }
}
