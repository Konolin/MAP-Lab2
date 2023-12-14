package map.project.musiclibrary;

import map.project.musiclibrary.data.model.audios.Advertisement;
import map.project.musiclibrary.data.model.audios.Podcast;
import map.project.musiclibrary.data.model.audios.PodcastPlaybackSpeedDecorator;
import map.project.musiclibrary.data.repository.PodcastRepository;
import map.project.musiclibrary.service.PodcastService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PlaySpeedDecoratorTest {
    @Autowired
    PodcastRepository podcastRepository;

    @Test
    public void testPlaySpeedDecorator() {
        Podcast podcast = podcastRepository.findAll().getFirst();
        String podcastName = podcast.getName();
        String podcastHostName = podcast.getHost().getName();
        String podcastSponsorName = Advertisement.listToString(podcast.getAdvertisements());
        double speed = 2.0;

        PodcastPlaybackSpeedDecorator podcastPlaybackSpeedDecorator = new PodcastPlaybackSpeedDecorator(podcast, speed);
        String expected = podcastPlaybackSpeedDecorator.play();

        assertEquals(expected, "Playing \"" + podcastName + "\" by " + podcastHostName + "\nSponsors: " + podcastSponsorName + "\nat " + speed + "x speed");
    }

    @Test
    public void testPlaySpeedDecoratorWithNullPodcast() {
        Podcast podcast = null;
        double speed = 2.0;

        PodcastPlaybackSpeedDecorator podcastPlaybackSpeedDecorator = new PodcastPlaybackSpeedDecorator(podcast, speed);
        assertThrows(NullPointerException.class, podcastPlaybackSpeedDecorator::play);
    }
}
