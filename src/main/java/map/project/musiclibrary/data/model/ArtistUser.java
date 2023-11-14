package map.project.musiclibrary.data.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
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
    @ManyToMany(mappedBy = "followedArtists", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<NormalUser> followers = new ArrayList<>();
    @OneToMany(mappedBy = "artist")
    private List<Album> albums = new ArrayList<>();

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
                ", birthdate=" + birthdate +
                ')';
    }

    public String toShortString() {
        return "(ID: " + id +
                ", Name: " + name +
                ")";
    }

    public void releaseAlbum(Album album) {
        albums.add(album);
        notifyFollowers(album);
    }

    public void notifyFollowers(Album album) {
        for (NormalUser follower : followers) {
            follower.update("New album released: " + album.getName() + " by " + getName());
        }
    }

    public void addFollower(NormalUser follower) {
        if (!followers.contains(follower)) {
            followers.add(follower);
            follower.followArtist(this);
        } else {
            throw new RuntimeException("Already following artist with ID " + getId());
        }
    }


    public void removeFollower(NormalUser follower) {
        if (followers.contains(follower)) {
            followers.remove(follower);
            follower.unfollowArtist(this);
        } else {
            throw new RuntimeException("Already not following artist with ID " + getId());
        }
    }

}
