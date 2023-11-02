package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabelRepository extends JpaRepository<Label, Long> {
    List<Label> findByName(String name);
}
