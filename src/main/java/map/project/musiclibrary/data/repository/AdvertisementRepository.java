package map.project.musiclibrary.data.repository;

import map.project.musiclibrary.data.model.audios.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByName(String name);

    @Query(value = "SELECT * FROM advertisments ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Advertisement findRandomEntity();
}
