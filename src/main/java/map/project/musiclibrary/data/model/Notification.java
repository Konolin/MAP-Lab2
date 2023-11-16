package map.project.musiclibrary.data.model;

import jakarta.persistence.*;
import lombok.Data;

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

    private String message;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    private boolean seen = false;

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    @Override
    public String toString() {
        return "Hey, " + user.getName() + "! New Album Release! Go listen "
                + album.getName() + " by " + album.getSongs().get(0).getArtist().getName() + " now!";
    }
}
