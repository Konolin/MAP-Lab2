package map.project.musiclibrary.data.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "Hosts")
public class HostUser extends User {
    @OneToMany(mappedBy = "host")
    private List<Podcast> podcasts;
}
