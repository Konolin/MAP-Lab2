package map.project.musiclibrary.data.repository.model;

import jakarta.persistence.*;

@Entity
@Table(name = "podcasts")
public class Podcast extends Audio {
    @Column(name = "topic")
    private String topic;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private Host host;
}
