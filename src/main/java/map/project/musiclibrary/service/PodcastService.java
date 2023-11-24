package map.project.musiclibrary.service;

import map.project.musiclibrary.data.model.audios.Advertisement;
import map.project.musiclibrary.data.model.audios.Podcast;
import map.project.musiclibrary.data.repository.AdvertisementRepository;
import map.project.musiclibrary.data.repository.PodcastRepository;
import map.project.musiclibrary.service.builders.PodcastBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PodcastService {
    private final PodcastRepository podcastRepository;
    private final AdvertisementRepository advertisementRepository;
    private final HostUserService hostUserService;

    @Autowired
    public PodcastService(PodcastRepository podcastRepository, AdvertisementRepository advertisementRepository, HostUserService hostUserService) {
        this.podcastRepository = podcastRepository;
        this.advertisementRepository = advertisementRepository;
        this.hostUserService = hostUserService;
    }

    public Podcast adPodcast(String name, String lengthStr, String topic, String releaseDateStr, String hostIdStr) throws IllegalArgumentException {
        Podcast podcast = new PodcastBuilder()
                .setName(name)
                .setLength(lengthStr)
                .setTopic(topic)
                .setReleaseDate(releaseDateStr)
                .setHostId(hostIdStr)
                .build(hostUserService);
        return podcastRepository.save(podcast);
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

    public Podcast addAdToPodcast(String adIdStr, String podcastIdStr) throws NumberFormatException {
        Long adId = Long.parseLong(adIdStr);
        Long podcastId = Long.parseLong(podcastIdStr);

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
