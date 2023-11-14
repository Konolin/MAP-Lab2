package map.project.musiclibrary.data.model;

import map.project.musiclibrary.data.repository.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlayableWithAds implements Playable {
    private final Advertisement advertisement;

    @Autowired
    public PlayableWithAds(AdvertisementRepository advertisementRepository) {
        this.advertisement = advertisementRepository.findRandomEntity();
    }

    @Override
    public String play(String name, String creatorUserName) {
        return "Playing ad \"" + advertisement.getName() + "\n" +
                "Playing \"" + name + "\" by " + creatorUserName;
    }
}
