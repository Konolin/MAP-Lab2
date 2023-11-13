package map.project.musiclibrary.data.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Users")
@Data
public class NormalUser extends User implements Observer {
    @Column(name = "isPremium")
    private boolean isPremium;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "credentials_id", referencedColumnName = "id")
    private LoginCredentials loginCredentials;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_followed_artists",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private List<ArtistUser> followedArtists = new ArrayList<>();

    @Override
    public String toString() {
        return "NormalUser(" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthdate=" + birthdate +
                ", isPremium=" + isPremium +
                // TODO - temporar
                ", email=" + loginCredentials.getEmail() +
                ", password=" + loginCredentials.getPassword() +
                ')';
    }

    public void followArtist(ArtistUser artist) {
        this.followedArtists.add(artist);
        artist.getFollowers().add(this);
    }

    public void unfollowArtist(ArtistUser artist) {
        this.followedArtists.remove(artist);
        artist.getFollowers().remove(this);
    }

    @Override
    public void update(String message) {
        receiveNotification(message);
    }

    public void receiveNotification(String message) {
        System.out.println("Notification for user " + getName() + ": " + message);
    }
}
