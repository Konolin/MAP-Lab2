package map.project.musiclibrary.data.model.audios;

import jakarta.persistence.*;
import lombok.Data;
import map.project.musiclibrary.data.model.users.NormalUser;

import java.util.List;

@Data
@Entity
@Table(name = "playlists")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "playlist_song",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )

    private List<Song> songs;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private NormalUser normalUser;

    public boolean addSong(Song song) {
        return songs.add(song);
    }

    public void setUser(NormalUser normalUser) {
        this.normalUser = normalUser;
    }
}