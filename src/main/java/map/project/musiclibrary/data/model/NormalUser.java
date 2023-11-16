package map.project.musiclibrary.data.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

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


    //TODO - putem folosi lista asta ca sa vedem si ce artisti urmareste un user
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
                // TODO - temporar
                ", email=" + loginCredentials.getEmail() +
                ", password=" + loginCredentials.getPassword() +
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

    public String seeNewNotifications() {
        StringBuilder result = new StringBuilder();
        Iterator<Notification> iterator = notifications.iterator();

        if (notifications.isEmpty()) {
            result.append("No new notifications.");
        } else {
            result.append("New Notifications:\n");
            while (iterator.hasNext()) {
                Notification notification = iterator.next();
                if (!notification.isSeen()) {
                    result.append(notification);
                    if (iterator.hasNext())
                        result.append('\n');
                    notification.setSeen(true);
                    iterator.remove();
                }
            }
        }
        return result.toString();
    }


}
