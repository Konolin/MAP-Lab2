package map.project.musiclibrary.service;

import map.project.musiclibrary.data.repository.HostUserRepository;
import map.project.musiclibrary.data.repository.model.HostUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
