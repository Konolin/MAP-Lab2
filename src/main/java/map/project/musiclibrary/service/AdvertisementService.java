package map.project.musiclibrary.service;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.audios.Advertisement;
import map.project.musiclibrary.data.model.audios.Podcast;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.data.repository.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final PodcastService podcastService;

    @Autowired
    public AdvertisementService(AdvertisementRepository advertisementRepository, PodcastService podcastService) {
        this.advertisementRepository = advertisementRepository;
        this.podcastService = podcastService;
    }

    public Advertisement addAdvertisement(UserSession userSession, String name, String lengthStr, String type, String releaseDateStr) throws ParseException {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            Advertisement advertisement = new Advertisement();

            advertisement.setName(name);
            try {
                int length = Integer.parseInt(lengthStr);
                advertisement.setLength(length);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Error: Invalid length format. Please use a number.");
            }
            advertisement.setAdvertisementType(type);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date releaseDate = dateFormat.parse(releaseDateStr);
            advertisement.setReleaseDate(releaseDate);

            return advertisementRepository.save(advertisement);
        }
        throw new RuntimeException("Only admin can add ads");
    }

    public Advertisement findByName(UserSession userSession, String name) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            return advertisementRepository.findByName(name).stream().findFirst().orElse(null);
        }
        throw new RuntimeException("Only admin can find ads");
    }

    public List<Advertisement> findAll(UserSession userSession) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            return advertisementRepository.findAll();
        }
        throw new RuntimeException("Only admin can list all ads");
    }

    public boolean delete(UserSession userSession, String idStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            Long id;
            try {
                id = Long.parseLong(idStr);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Error: Invalid id format. Please use a number.");
            }

            Optional<Advertisement> optional = advertisementRepository.findById(id);
            if (optional.isPresent()) {
                Advertisement advertisement = optional.get();

                // remove the links between the podcasts and ad
                Iterator<Podcast> iterator = advertisement.getPodcasts().iterator();
                while (iterator.hasNext()) {
                    Podcast podcast = iterator.next();
                    podcast.getAdvertisements().remove(advertisement);
                    podcastService.save(podcast);
                    iterator.remove();
                }

                advertisementRepository.deleteById(id);
                return true;
            } else {
                throw new EntityNotFoundException("Ad was not found");
            }
        }
        throw new RuntimeException("Only admin can delete ads");
    }
}
