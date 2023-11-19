package map.project.musiclibrary;

import map.project.musiclibrary.data.model.users.ArtistUser;
import map.project.musiclibrary.data.model.strategies.PlayableWithAds;
import map.project.musiclibrary.data.model.strategies.PlayableWithoutAds;
import map.project.musiclibrary.data.model.audios.Song;
import map.project.musiclibrary.data.repository.AdvertisementRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PlayableStrategyTest {
    AdvertisementRepository advertisementRepository;

    @Autowired
    public PlayableStrategyTest(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    @Test
    public void playStrategyTest() {
        ArtistUser artist = new ArtistUser();
        artist.setName("artist");

        Song song = new Song();
        song.setArtist(artist);
        song.setName("song");

        String withoutAd = song.play(new PlayableWithoutAds());
        String withAd = song.play(new PlayableWithAds(advertisementRepository));

        assertEquals(withoutAd, "Playing \"song\" by artist");
        assertEquals(withAd, "Playing ad \nPlaying \"song\" by artist");
    }
}
