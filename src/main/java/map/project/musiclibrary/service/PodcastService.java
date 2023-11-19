package map.project.musiclibrary.service;

import map.project.musiclibrary.data.model.audios.Advertisement;
import map.project.musiclibrary.data.model.audios.Podcast;
import map.project.musiclibrary.data.repository.AdvertisementRepository;
import map.project.musiclibrary.data.repository.PodcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PodcastService {
    private final PodcastRepository podcastRepository;
    private final AdvertisementRepository advertisementRepository;
    private final NormalUserService normalUserService;

    @Autowired
    public PodcastService(PodcastRepository podcastRepository, AdvertisementRepository advertisementRepository, NormalUserService normalUserService) {
        this.podcastRepository = podcastRepository;
        this.advertisementRepository = advertisementRepository;
        this.normalUserService = normalUserService;
    }

    public Podcast save(Podcast podcast) {
        return podcastRepository.save(podcast);
    }

    public Podcast findByName(String name) {
        return podcastRepository.findByName(name).stream().findFirst().orElse(null);
    }

    public List<Podcast> findAll() {
        return podcastRepository.findAll();
    }

    public Podcast addAd(Long adId, Long podcastId) {
        // search for the podcast and advertisement with the corresponding ids
        Optional<Podcast> podcastOptional = podcastRepository.findById(podcastId);
        Optional<Advertisement> advertisementOptional = advertisementRepository.findById(adId);

        if (podcastOptional.isPresent() && advertisementOptional.isPresent()) {
            // get the podcast and advertisement with the corresponding ids
            Podcast podcast = podcastOptional.get();
            Advertisement advertisement = advertisementOptional.get();
            // add advertisement to the podcast list of ads
            podcast.addAdvertisement(advertisement);
            // update the podcast in repo
            return podcastRepository.save(podcast);
        }

        throw new RuntimeException("PodcastService::Advertisement or podcast with specified id doesn't exist");
    }

    public String playPodcast(String podcastIdStr) {
        try {
            Long podcastId = Long.parseLong(podcastIdStr);
            Optional<Podcast> podcastOptional = podcastRepository.findById(podcastId);

            if (podcastOptional.isPresent()) {
                return podcastOptional.get().play();
            }
            return "Podcast not found";
        } catch (NumberFormatException e) {
            return "Invalid id";
        }
    }
}
