package map.project.musiclibrary.service;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.users.ArtistUser;
import map.project.musiclibrary.data.model.users.NormalUser;
import map.project.musiclibrary.data.repository.ArtistUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistUserService {
    final ArtistUserRepository artistUserRepository;

    @Autowired
    public ArtistUserService(ArtistUserRepository artistUserRepository) {
        this.artistUserRepository = artistUserRepository;
    }

    public ArtistUser addArtist(String name, String birthdateStr) throws ParseException {
        ArtistUser artist = new ArtistUser();
        artist.setName(name);
        artist.setSongs(new ArrayList<>());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthdate = dateFormat.parse(birthdateStr);
        artist.setBirthdate(birthdate);

        return artistUserRepository.save(artist);
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

    public Optional<ArtistUser> findById(Long id) {
        return artistUserRepository.findById(id);
    }

    public List<NormalUser> getFollowers(String artistIdStr) throws NumberFormatException {
        Long artistId = Long.parseLong(artistIdStr);
        Optional<ArtistUser> artistUserOptional = findById(artistId);
        if (artistUserOptional.isPresent()) {
            ArtistUser artist = artistUserOptional.get();
            return artist.getFollowers();
        } else {
            throw new EntityNotFoundException("User with specified id not found");
        }
    }
}
