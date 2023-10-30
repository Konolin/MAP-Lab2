package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.repository.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
}
