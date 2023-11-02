package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.model.NormalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NormalUserRepository extends JpaRepository<NormalUser, Long> {
    List<NormalUser> findByName(String name);
}
