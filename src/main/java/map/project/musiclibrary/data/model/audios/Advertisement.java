package map.project.musiclibrary.data.model.audios;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private List<Podcast> podcasts;

    public static String listToString(List<Advertisement> advertisements) {
        String adsString = "[]";
        if (advertisements != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (Advertisement advertisement : advertisements) {
                sb.append(advertisement.toShortString()).append(", ");
            }
            if (!advertisements.isEmpty()) {
                sb.delete(sb.length() - 2, sb.length());
            }
            sb.append("]");
            adsString = sb.toString();
        }
        return adsString;
    }

    @Override
    public String toString() {
        // special string format for podcast list to stop unnecessary data
        // in the String representation and stop infinite loops
        return "Advertisement{" +
                "id=" + id +
                ", advertisementType='" + advertisementType + '\'' +
                ", podcasts=" + Podcast.listToString(podcasts) +
                ", name='" + name + '\'' +
                ", length=" + length +
                ", releaseDate=" + releaseDate +
                '}';
    }

    public String toShortString() {
        return "(ID: " + id +
                ", Name: " + name +
                ", Type: " + advertisementType +
                ")";
    }

    // TODO - ad type enum
}
