package map.project.musiclibrary.data.model.users;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import map.project.musiclibrary.data.model.misc.Notification;
import map.project.musiclibrary.data.model.observer.Observer;

import java.util.ArrayList;
import java.util.Iterator;
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

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();

    @Override
    public String toString() {
        return "NormalUser(" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthdate=" + birthdate +
                ", isPremium=" + isPremium +
                ')';
    }

    public void followArtist(ArtistUser artist) {
        if (!followedArtists.contains(artist)) {
            this.followedArtists.add(artist);
        }
    }

    public void unfollowArtist(ArtistUser artist) {
        this.followedArtists.remove(artist);
    }

    @Override
    public void update(String message) {
        receiveNotification(message);
    }

    public void receiveNotification(String message) {
        System.out.println("Notification for user " + getName() + ": " + message);
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }
}
