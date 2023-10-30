package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.repository.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {
}
