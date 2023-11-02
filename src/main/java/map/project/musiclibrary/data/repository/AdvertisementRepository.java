package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByName(String name);
}
