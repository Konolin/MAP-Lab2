package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.repository.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Long> {
}
