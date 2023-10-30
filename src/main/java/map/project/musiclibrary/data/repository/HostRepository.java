package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.repository.model.Host;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostRepository extends JpaRepository<Host, Long> {
}
