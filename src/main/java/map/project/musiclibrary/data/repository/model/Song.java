package map.project.musiclibrary.data.repository.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "songs")
@Data
public class Song extends Audio {
    @Column(name = "genre")
    private String genre;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;
}
