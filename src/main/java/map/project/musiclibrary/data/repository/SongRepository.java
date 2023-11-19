package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.model.audios.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByName(String name);

    Optional<Song> findById(Long id);
}
