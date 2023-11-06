package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.model.LoginCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoginCredentialsRepository extends JpaRepository<LoginCredentials, Long> {
    List<LoginCredentials> findByEmailAndPassword (String email, String password);
}
