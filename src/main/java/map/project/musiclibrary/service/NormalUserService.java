package map.project.musiclibrary.service;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.ArtistUser;
import map.project.musiclibrary.data.model.LoginCredentials;
import map.project.musiclibrary.data.model.NormalUser;
import map.project.musiclibrary.data.repository.LoginCredentialsRepository;
import map.project.musiclibrary.data.repository.NormalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NormalUserService {
    private final NormalUserRepository normalUserRepository;
    private final LoginCredentialsRepository loginCredentialsRepository;
    private final ArtistUserService artistUserService;

    @Autowired
    public NormalUserService(NormalUserRepository normalUserRepository, LoginCredentialsRepository loginCredentialsRepository, ArtistUserService artistUserService) {
        this.normalUserRepository = normalUserRepository;
        this.loginCredentialsRepository = loginCredentialsRepository;
        this.artistUserService = artistUserService;
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

    public NormalUser login(String email, String password) {
        List<LoginCredentials> loginCredentialsList = loginCredentialsRepository.findByEmailAndPassword(email, password);
        if (loginCredentialsList.isEmpty()) {
            return null;
        }
        return loginCredentialsList.get(0).getUser();
    }

    @Transactional
    public void followArtist(NormalUser user, Long artistId) {
        Optional<ArtistUser> artistUserOptional = artistUserService.findById(artistId);
        Optional<NormalUser> userOptional = normalUserRepository.findById(user.getId());

        if (artistUserOptional.isPresent() && userOptional.isPresent()) {
            ArtistUser artist = artistUserOptional.get();
            NormalUser currentUser = userOptional.get();
            artist.addFollower(currentUser);
            artistUserService.save(artist);
        } else {
            throw new EntityNotFoundException("Artist or user not found.");
        }
    }

    @Transactional
    public void unfollowArtist(NormalUser user, Long artistId) {
        Optional<ArtistUser> artistUserOptional = artistUserService.findById(artistId);
        Optional<NormalUser> userOptional = normalUserRepository.findById(user.getId());

        if (artistUserOptional.isPresent() && userOptional.isPresent()) {
            ArtistUser artist = artistUserOptional.get();
            NormalUser currentUser = userOptional.get();
            artist.removeFollower(currentUser);
            artistUserService.save(artist);
        } else {
            throw new EntityNotFoundException("Artist or user not found.");
        }
    }



}
