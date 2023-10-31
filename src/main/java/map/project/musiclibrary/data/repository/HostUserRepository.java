package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.repository.model.HostUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostUserRepository extends JpaRepository<HostUser, Long> {
}
