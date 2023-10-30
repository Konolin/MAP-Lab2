package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.repository.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name);
}
