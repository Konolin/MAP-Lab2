package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.model.users.HostUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HostUserRepository extends JpaRepository<HostUser, Long> {
    List<HostUser> findByName(String name);

    Optional<HostUser> findById(Long id);
}
