package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByName(String name);
}
