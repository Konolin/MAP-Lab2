package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.model.audios.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByName(String name);
}
