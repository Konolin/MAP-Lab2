package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.repository.model.ArtistUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistUserRepository extends JpaRepository<ArtistUser, Long> {
}
