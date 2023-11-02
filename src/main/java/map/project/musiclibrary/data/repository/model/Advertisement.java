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
    @Column(name = "type")
    private String advertisementType;

    @ManyToMany(mappedBy = "advertisements", fetch = FetchType.EAGER)
    private List<Podcast> podcasts;

    @Override
    public String toString() {
        return "Advertisement{" +
                "id=" + id +
                ", advertisementType='" + advertisementType + '\'' +
                ", podcasts=" + podcasts +
                ", name='" + name + '\'' +
                ", length=" + length +
                ", releaseDate=" + releaseDate +
                '}';
    }
}
