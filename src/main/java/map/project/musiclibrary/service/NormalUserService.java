package map.project.musiclibrary.service;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.*;
import map.project.musiclibrary.data.repository.LoginCredentialsRepository;
import map.project.musiclibrary.data.repository.NormalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

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

    public void playAudio(Playable playable, boolean isPremium) {
        Random randomChance = new Random();
        if (!isPremium && randomChance.nextInt(100) <= 33) {  //33 represents the chance of an ad pop-up (33%)
            Advertisement ad = new Advertisement(); // TODO - get random ad from database
            ad.play();

            try {
                Thread.sleep(ad.getLength());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Now playing " + ((Audio) playable).getName());
        playable.play();
    }

    @Transactional
    public void followArtist(NormalUser user, Long artistId) {
        Optional<ArtistUser> artistUserOptional = artistUserService.findById(artistId);
        Optional<NormalUser> userOptional = normalUserRepository.findById(user.getId());

        if (artistUserOptional.isPresent() && userOptional.isPresent()) {
            ArtistUser artist = artistUserOptional.get();
            NormalUser currentUser = userOptional.get();

            artist.addFollower(currentUser);
            //currentUser.followArtist(artist);

            artistUserService.save(artist);
            //save(currentUser);
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
            //currentUser.unfollowArtist(artist);

            artistUserService.save(artist);
            //save(currentUser);
        } else {
            throw new EntityNotFoundException("Artist or user not found.");
        }
    }



}
