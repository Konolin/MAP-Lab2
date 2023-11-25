package map.project.musiclibrary.data.model.users;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import map.project.musiclibrary.data.model.audios.Album;
import map.project.musiclibrary.data.model.audios.Song;
import map.project.musiclibrary.data.model.misc.Label;
import map.project.musiclibrary.data.model.misc.Notification;

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
                ", label=" + labelStr + // TODO lable short string
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

    public void notifyFollowers(Album album) {
        for (NormalUser follower : followers) {
            String message = "New album released: " + album.getName() + " by " + getName();
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setUser(follower);
            notification.setAlbum(album);
            follower.update(message);
            follower.addNotification(notification);
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
