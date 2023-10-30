package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.repository.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
