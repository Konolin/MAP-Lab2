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
        String podcastsString = "[]";
        if (podcasts != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (Podcast podcast : podcasts) {
                sb.append("(")
                        .append("ID: ").append(podcast.getId())
                        .append(", Name: ").append(podcast.getName())
                        .append(", Topic: ").append(podcast.getTopic())
                        .append("), ");
            }
            if (!podcasts.isEmpty()) {
                sb.delete(sb.length() - 2, sb.length());
            }
            sb.append("])");
            podcastsString = sb.toString();
        }

        return "HostUser(" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthdate=" + birthdate +
                ", podcasts=" + podcastsString +
                ')';
    }

    public boolean addPodcast(Podcast podcast) {
        return podcasts.add(podcast);
    }
}
