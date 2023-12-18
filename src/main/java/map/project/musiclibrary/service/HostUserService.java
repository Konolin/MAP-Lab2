package map.project.musiclibrary.service;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.audios.Podcast;
import map.project.musiclibrary.data.model.users.HostUser;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.data.repository.HostUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HostUserService {
    private final HostUserRepository hostUserRepository;
    private final PodcastService podcastService;

    @Autowired
    public HostUserService(HostUserRepository hostUserRepository, @Lazy PodcastService podcastService) {
        this.hostUserRepository = hostUserRepository;
        this.podcastService = podcastService;
    }

    public HostUser addHost(String name, String birthdateStr) {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isAdmin()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date birthdate;
            try {
                birthdate = dateFormat.parse(birthdateStr);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid birthdate format. Please use yyyy-MM-dd.");
            }

            HostUser host = (HostUser) CreatorUserFactory.createCreatorUser("host", name, birthdate);

            return hostUserRepository.save(host);
        }
        throw new SecurityException("You must log in as an admin to add a host.");
    }

    public void deleteHost(String idStr) throws NumberFormatException {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isAdmin()) {
            Long id = Long.parseLong(idStr);
            Optional<HostUser> hostUserOptional = hostUserRepository.findById(id);

            if (hostUserOptional.isPresent()) {
                HostUser host = hostUserOptional.get();

                for (Podcast podcast : host.getPodcasts()) {
                    podcastService.deletePodcast(podcast.getId().toString());
                }
                host.getPodcasts().clear();
                hostUserRepository.deleteById(id);
            }
            throw new EntityNotFoundException("Host with ID " + id + " was not found.");
        }
        throw new SecurityException("You must log in as an admin to delete a host.");
    }

    public HostUser save(HostUser hostUser) {
        return hostUserRepository.save(hostUser);
    }

    public HostUser findByName(String name) {
        return hostUserRepository.findByName(name).stream().findFirst().orElse(null);
    }

    public List<HostUser> findAll() {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isAdmin()) {
            return hostUserRepository.findAll();
        }
        throw new SecurityException("You must log in as an admin to list all hosts.");
    }

    public Optional<HostUser> findById(Long id) {
        return hostUserRepository.findById(id);
    }

    public List<Podcast> listHostsPodcasts(String idStr) throws NumberFormatException {
        Long id = Long.parseLong(idStr);
        Optional<HostUser> hostUserOptional = hostUserRepository.findById(id);
        if (hostUserOptional.isPresent()) {
            return hostUserOptional.get().getPodcasts();
        }
        throw new EntityNotFoundException("HostUserService::Host with specified id doesn't exist");
    }
}
