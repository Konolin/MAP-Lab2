package map.project.musiclibrary.data.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Hosts")
@Data
public class HostUser extends User {
    @OneToMany(mappedBy = "host")
    private List<Podcast> podcasts;

    @Override
    public String toString() {
        return "HostUser(" +
                "podcasts=" + podcasts +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthdate=" + birthdate +
                ')';
    }
}
