package map.project.musiclibrary.service;

import map.project.musiclibrary.data.repository.AdvertisementRepository;
import map.project.musiclibrary.data.repository.PodcastRepository;
import map.project.musiclibrary.data.repository.model.Advertisement;
import map.project.musiclibrary.data.repository.model.HostUser;
import map.project.musiclibrary.data.repository.model.Podcast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PodcastService {
    private final PodcastRepository podcastRepository;
    private final AdvertisementRepository advertisementRepository;

    @Autowired
    public PodcastService(PodcastRepository podcastRepository, AdvertisementRepository advertisementRepository) {
        this.podcastRepository = podcastRepository;
        this.advertisementRepository = advertisementRepository;
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
        Optional<Podcast> podcastOptional = podcastRepository.findById(podcastId);
        Optional<Advertisement> advertisementOptional = advertisementRepository.findById(adId);

        if (podcastOptional.isPresent() && advertisementOptional.isPresent()) {
            Podcast podcast = podcastOptional.get();
            Advertisement advertisement = advertisementOptional.get();
            podcast.addAdvertisement(advertisement);
            return podcastRepository.save(podcast);
        }

        throw new RuntimeException("PodcastService::Advertisement or podcast with specified id doesn't exist");
    }
}
