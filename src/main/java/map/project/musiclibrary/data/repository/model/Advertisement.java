package map.project.musiclibrary.data.repository.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "advertisments")
public class Advertisement extends Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String advertisementType;

    @ManyToMany(mappedBy = "advertisements", fetch = FetchType.EAGER)
    private List<Podcast> podcasts;
}
