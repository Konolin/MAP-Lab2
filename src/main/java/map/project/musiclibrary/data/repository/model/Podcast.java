package map.project.musiclibrary.data.repository.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "podcasts")
@Data
public class Podcast extends Audio {
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
        String advertisementsString = "[]";
        if (advertisements != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (Advertisement ad : advertisements) {
                sb.append("(")
                        .append("ID: ").append(ad.getId())
                        .append(", Name: ").append(ad.getName())
                        .append(", Type: ").append(ad.getAdvertisementType())
                        .append("), ");
            }
            if (!advertisements.isEmpty()) {
                sb.delete(sb.length() - 2, sb.length());
            }
            sb.append("]");
            advertisementsString = sb.toString();
        }

        String hostStr = "[Id: " + host.getId() + ", Name: " + host.getName() + ']';

        return "Podcast(" +
                "id=" + id +
                ", topic='" + topic + '\'' +
                ", host=" + hostStr +
                ", advertisements=" + advertisementsString +
                ", name='" + name + '\'' +
                ", length=" + length +
                ", releaseDate=" + releaseDate +
                ')';
    }

    public boolean addAdvertisement(Advertisement advertisement) {
        return advertisements.add(advertisement);
    }
}
