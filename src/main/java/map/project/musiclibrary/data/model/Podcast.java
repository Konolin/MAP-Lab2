package map.project.musiclibrary.data.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "podcasts")
@Data
public class Podcast extends Audio implements Playable{
    @Column(name = "topic")
    private String topic;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private HostUser host;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "podcast_advertisement",
            joinColumns = @JoinColumn(name = "podcast_id"),
            inverseJoinColumns = @JoinColumn(name = "advertisement_id")
    )
    private List<Advertisement> advertisements;

    @Override
    public String toString() {
        // special string format for advertisement list and host to stop unnecessary data
        // in the String representation and stop infinite loops
        String advertisementsString = "[]";
        if (advertisements != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (Advertisement ad : advertisements) {
                sb.append(ad.toShortString()).append("), ");
            }
            if (!advertisements.isEmpty()) {
                sb.delete(sb.length() - 2, sb.length());
            }
            sb.append("]");
            advertisementsString = sb.toString();
        }
        return "Podcast(" +
                "id=" + id +
                ", topic='" + topic + '\'' +
                ", host=" + host.toShortString() +
                ", advertisements=" + advertisementsString +
                ", name='" + name + '\'' +
                ", length=" + length +
                ", releaseDate=" + releaseDate +
                ')';
    }

    public boolean addAdvertisement(Advertisement advertisement) {
        return advertisements.add(advertisement);
    }

    public String toShortString() {
        return "(ID: " + id +
                ", Name: " + name +
                ", Topic: " + topic +
                ")";
    }

    public static String listToString(List<Podcast> podcasts) {
        String podcastsString = "[]";
        if (podcasts != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (Podcast podcast : podcasts) {
                sb.append(podcast.toShortString()).append(", ");
            }
            if (!podcasts.isEmpty()) {
                sb.delete(sb.length() - 2, sb.length());
            }
            sb.append("])");
            podcastsString = sb.toString();
        }
        return podcastsString;
    }

    @Override
    public void play(){
        System.out.printf("Playing podcast %s by %s", name, host.getName());
    }
}
