package map.project.musiclibrary;

import map.project.musiclibrary.data.model.audios.Podcast;
import map.project.musiclibrary.service.HostUserService;
import map.project.musiclibrary.service.builders.PodcastBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
// TODO - fix la asta (no bean found)
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PodcastBuilderTest {
    @Autowired
    HostUserService hostUserService;

    @Test
    public void buildPodcastSuccessfully() {
        PodcastBuilder pb = new PodcastBuilder()
                .setName("Test Podcast")
                .setLength("30")
                .setTopic("Test Topic")
                .setReleaseDate("2023-01-01")
                .setHostId("1");
        Podcast podcast = pb.build(hostUserService);

        assertEquals("Test Podcast", podcast.getName());
        assertEquals(30, podcast.getLength());
        assertEquals("Test Topic", podcast.getTopic());
        assertEquals(parseDate("2023-01-01"), podcast.getReleaseDate());
        assertNotNull(podcast.getHost());
        assertEquals(1L, podcast.getHost().getId());
    }

    @Test
    void buildPodcastWithInvalidLength() {
        PodcastBuilder podcastBuilder = new PodcastBuilder()
                .setName("Test Podcast")
                .setLength("invalid")
                .setTopic("Test Topic")
                .setReleaseDate("2023-01-01")
                .setHostId("1");

        assertThrows(IllegalArgumentException.class, () -> podcastBuilder.build(hostUserService));
    }

    @Test
    void buildPodcastWithNegativeLength() {
        PodcastBuilder podcastBuilder = new PodcastBuilder()
                .setName("Test Podcast")
                .setLength("-190")
                .setTopic("Test Topic")
                .setReleaseDate("2023-01-01")
                .setHostId("1");

        assertThrows(IllegalArgumentException.class, () -> podcastBuilder.build(hostUserService));
    }

    @Test
    void buildPodcastWithInvalidReleaseDate() {
        PodcastBuilder podcastBuilder = new PodcastBuilder()
                .setName("Test Podcast")
                .setLength("30")
                .setTopic("Test Topic")
                .setReleaseDate("invalid")
                .setHostId("1");

        assertThrows(IllegalArgumentException.class, () -> podcastBuilder.build(hostUserService));
    }

    @Test
    void buildPodcastWithInvalidHostId() {
        PodcastBuilder podcastBuilder = new PodcastBuilder()
                .setName("Test Podcast")
                .setLength("30")
                .setTopic("Test Topic")
                .setReleaseDate("2023-01-01")
                .setHostId("invalid");

        assertThrows(IllegalArgumentException.class, () -> podcastBuilder.build(hostUserService));
    }

    private Date parseDate(String dateString) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }
}
