package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.repository.model.Advertisement;
import map.project.musiclibrary.data.repository.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByName(String name);
}
