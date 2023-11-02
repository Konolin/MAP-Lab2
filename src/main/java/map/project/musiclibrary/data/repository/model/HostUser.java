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
        return "HostUser(" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthdate=" + birthdate +
                ')';
    }

    public String podcastsToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hosts podcasts=[");
        if (podcasts != null) {
            for (Podcast podcast : podcasts) {
                sb.append(podcast.toString()).append(", ");
            }
        } else {
            sb.append("null");
        }
        sb.append("])");
        return sb.toString();
    }

    public boolean addPodcast(Podcast podcast) {
        return podcasts.add(podcast);
    }
}
