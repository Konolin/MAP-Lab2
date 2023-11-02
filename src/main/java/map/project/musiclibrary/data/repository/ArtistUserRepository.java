package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.model.ArtistUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistUserRepository extends JpaRepository<ArtistUser, Long> {
    List<ArtistUser> findByName(String name);
}
