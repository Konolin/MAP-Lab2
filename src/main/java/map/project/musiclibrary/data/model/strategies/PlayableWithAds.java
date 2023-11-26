package map.project.musiclibrary.data.model.strategies;

import map.project.musiclibrary.data.repository.AdvertisementRepository;

// TODO - add random ad
public class PlayableWithAds implements Playable {
    // private final Advertisement advertisement;

    public PlayableWithAds(AdvertisementRepository advertisementRepository) {
        // this.advertisement = advertisementRepository.findRandomEntity();
    }

    @Override
    public String play(String name, String creatorUserName) {
        // TODO - 40% sansa sa pice o reclama
        // TODO - update test cand adaugam reclama random
        return //"Playing ad \"" + advertisement.getName() + "\n" +
                "Playing ad \n" +
                        "Playing \"" + name + "\" by " + creatorUserName;
    }
}
