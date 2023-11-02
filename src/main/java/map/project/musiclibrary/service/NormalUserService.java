package map.project.musiclibrary.service;

import map.project.musiclibrary.data.repository.NormalUserRepository;
import map.project.musiclibrary.data.model.NormalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NormalUserService {
    private final NormalUserRepository normalUserRepository;

    @Autowired
    public NormalUserService(NormalUserRepository normalUserRepository) {
        this.normalUserRepository = normalUserRepository;
    }

    public NormalUser save(NormalUser user) {
        return normalUserRepository.save(user);
    }

    public NormalUser findByName(String name) {
        return normalUserRepository.findByName(name).stream().findFirst().orElse(null);
    }

    public List<NormalUser> findAll() {
        return normalUserRepository.findAll();
    }
}
