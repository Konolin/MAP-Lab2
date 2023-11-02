package map.project.musiclibrary.entities;

import map.project.musiclibrary.data.repository.model.Advertisement;
import map.project.musiclibrary.data.repository.model.Podcast;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@DataJpaTest
class AdvertisementTest {
    Advertisement advertisement = new Advertisement();

    @Test
    void testAdvertisementEntity() {

        //test getters + setters
        advertisement.setId(1L);  //L in this case represents a cast to Long
        advertisement.setName("Song");
        advertisement.setLength(5);
        advertisement.setReleaseDate(new Date());
        advertisement.setAdvertisementType("Gambling");
        advertisement.setPodcasts(List.of(new Podcast()));

        Long id = advertisement.getId();
        String name = advertisement.getName();
        Integer length = advertisement.getLength();
        Date releaseDate = advertisement.getReleaseDate();
        String type = advertisement.getAdvertisementType();
        List<Podcast> podcasts = advertisement.getPodcasts();

        //assert that the retrieved values match the expected values
        assertEquals(1L, id);
        assertEquals("Song", name);
        assertEquals(5, length);
        assertNotNull(releaseDate);
        assertEquals("Gambling", type);
        assertNotNull(podcasts);
    }

    @Test
    void testAdvertisementException() {
        //test exception handling
        assertThrows(IllegalArgumentException.class, () -> advertisement.setReleaseDate(null));
    }
}