package map.project.musiclibrary.data.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
    @OneToMany(mappedBy = "host", fetch = FetchType.EAGER)
    private List<Podcast> podcasts;

    @Override
    public String toString() {
        // special string format for podcast list to stop unnecessary data
        // in the String representation and stop infinite loops
        return "HostUser(" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthdate=" + birthdate +
                ", podcasts=" + Podcast.listToString(podcasts) +
                ')';
    }

    public String toShortString() {
        return "(Id: " + id + ", Name: " + name + ')';
    }

    public boolean addPodcast(Podcast podcast) {
        return podcasts.add(podcast);
    }
}
