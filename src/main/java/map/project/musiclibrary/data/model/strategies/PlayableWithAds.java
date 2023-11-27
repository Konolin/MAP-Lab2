package map.project.musiclibrary.data.model.strategies;

import map.project.musiclibrary.data.model.audios.Advertisement;
import map.project.musiclibrary.data.repository.AdvertisementRepository;

import java.util.Random;

public class PlayableWithAds implements Playable {
    private final double AD_CHANCE = 0.4;
    private final Advertisement advertisement;

    public PlayableWithAds(AdvertisementRepository advertisementRepository) {
        this.advertisement = advertisementRepository.findRandomEntity();
    }

    @Override
    public String play(String name, String creatorUserName) {
        Random random = new Random();
        String adStr = random.nextDouble() < AD_CHANCE ? "Playing ad " + advertisement.getName() + '\n' : "";
        return adStr + "Playing \"" + name + "\" by " + creatorUserName;
    }
}
