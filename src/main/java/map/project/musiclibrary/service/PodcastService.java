package map.project.musiclibrary.service;

import map.project.musiclibrary.data.repository.PodcastRepository;
import map.project.musiclibrary.data.repository.model.Podcast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PodcastService {
    private final PodcastRepository podcastRepository;

    @Autowired
    public PodcastService(PodcastRepository podcastRepository) {
        this.podcastRepository = podcastRepository;
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
}
