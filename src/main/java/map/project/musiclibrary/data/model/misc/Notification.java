package map.project.musiclibrary.data.model.misc;

import jakarta.persistence.*;
import lombok.Data;
import map.project.musiclibrary.data.model.audios.Album;
import map.project.musiclibrary.data.model.users.NormalUser;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private NormalUser user;

    @Column(name = "message")
    private String message;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @Override
    public String toString() {
        return "Hey, " + user.getName() + "! New Album Release! Go listen "
                + album.getName() + " by " + album.getSongs().get(0).getArtist().getName() + " now!";
    }
}
