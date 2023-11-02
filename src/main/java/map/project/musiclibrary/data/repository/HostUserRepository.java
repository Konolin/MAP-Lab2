package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.repository.model.Advertisement;
import map.project.musiclibrary.data.repository.model.HostUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HostUserRepository extends JpaRepository<HostUser, Long> {
    List<HostUser> findByName(String name);
}
