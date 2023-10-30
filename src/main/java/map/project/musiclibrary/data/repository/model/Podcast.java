package map.project.musiclibrary.data.repository.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "podcasts")
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
}
