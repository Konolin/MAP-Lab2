package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.repository.model.Podcast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PodcastRepository extends JpaRepository<Podcast, Long> {
}