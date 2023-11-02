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

    @ManyToMany
    @JoinTable(
            name = "podcast_advertisement",
            joinColumns = @JoinColumn(name = "podcast_id"),
            inverseJoinColumns = @JoinColumn(name = "advertisement_id")
    )
    private List<Advertisement> advertisements;

    @Override
    public String toString() {
        return "Podcast(" +
                "topic='" + topic + '\'' +
                ", host=" + host +
                ", advertisements=" + advertisements +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", length=" + length +
                ", releaseDate=" + releaseDate +
                ')';
    }
}
