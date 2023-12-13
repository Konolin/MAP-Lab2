package map.project.musiclibrary.service;

import map.project.musiclibrary.data.model.audios.Podcast;
import map.project.musiclibrary.data.model.users.HostUser;
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

    public HostUser addHost(String name, String birthdateStr) throws ParseException {
        HostUser host = new HostUser();
        host.setName(name);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthdate = dateFormat.parse(birthdateStr);
        host.setBirthdate(birthdate);

        return hostUserRepository.save(host);
    }

    public void deleteHost(Long id) {
        Optional<HostUser> hostUserOptional = hostUserRepository.findById(id);

        if (hostUserOptional.isPresent()) {
            HostUser host = hostUserOptional.get();

            for (Podcast podcast : host.getPodcasts()) {
                podcastService.deletePodcast(podcast.getId());
            }
            host.getPodcasts().clear();
            hostUserRepository.deleteById(id);
        }
    }

    public HostUser save(HostUser hostUser) {
        return hostUserRepository.save(hostUser);
    }

    public HostUser findByName(String name) {
        return hostUserRepository.findByName(name).stream().findFirst().orElse(null);
    }

    public List<HostUser> findAll() {
        return hostUserRepository.findAll();
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
        throw new RuntimeException("HostUserService::Host with specified id doesn't exist");
    }
}
