package map.project.musiclibrary.service;

import map.project.musiclibrary.data.repository.ArtistUserRepository;
import map.project.musiclibrary.data.model.ArtistUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistUserService {
    final ArtistUserRepository artistUserRepository;

    @Autowired
    public ArtistUserService(ArtistUserRepository artistUserRepository) {
        this.artistUserRepository = artistUserRepository;
    }

    public ArtistUser save(ArtistUser artistUser) {
        return artistUserRepository.save(artistUser);
    }

    public ArtistUser findByName(String name) {
        return artistUserRepository.findByName(name).stream().findFirst().orElse(null);
    }

    public List<ArtistUser> findAll() {
        return artistUserRepository.findAll();
    }
}
