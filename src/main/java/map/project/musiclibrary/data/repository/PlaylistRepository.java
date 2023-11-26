package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.model.users.NormalUser;
import map.project.musiclibrary.data.model.audios.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByName(String name);

    List<Playlist> findByNormalUser(NormalUser user);

    // TODO - add delete option
}
