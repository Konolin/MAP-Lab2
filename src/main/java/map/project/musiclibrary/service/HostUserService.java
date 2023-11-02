package map.project.musiclibrary.service;

import map.project.musiclibrary.data.repository.HostUserRepository;
import map.project.musiclibrary.data.model.HostUser;
import map.project.musiclibrary.data.model.Podcast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HostUserService {
    private final HostUserRepository hostUserRepository;

    @Autowired
    public HostUserService(HostUserRepository hostUserRepository) {
        this.hostUserRepository = hostUserRepository;
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

    public List<Podcast> listHostsPodcasts(Long id) {
        Optional<HostUser> hostUserOptional = hostUserRepository.findById(id);
        if (hostUserOptional.isPresent()) {
            return hostUserOptional.get().getPodcasts();
        }
        throw new RuntimeException("HostUserService::Host with specified id doesn't exist");
    }
}
