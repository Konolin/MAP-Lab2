package map.project.musiclibrary.data.repository.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "advertisments")
public class Advertisement extends Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @Column(name = "company")
    private String company;

    @Column(name = "type")
    private String advertisementType;

    @ManyToMany(mappedBy = "advertisements")
    private List<Podcast> podcasts;
}
