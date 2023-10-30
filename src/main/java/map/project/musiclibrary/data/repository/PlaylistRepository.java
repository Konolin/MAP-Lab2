package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.repository.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
