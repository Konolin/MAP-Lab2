package map.project.musiclibrary.service;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.audios.Album;
import map.project.musiclibrary.data.model.audios.Song;
import map.project.musiclibrary.data.model.users.ArtistUser;
import map.project.musiclibrary.data.model.users.NormalUser;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.data.repository.ArtistUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistUserService {
    private final ArtistUserRepository artistUserRepository;
    private final SongService songService;
    private final AlbumService albumService;

    @Autowired
    public ArtistUserService(ArtistUserRepository artistUserRepository, @Lazy SongService songService, @Lazy AlbumService albumService) {
        this.artistUserRepository = artistUserRepository;
        this.songService = songService;
        this.albumService = albumService;
    }

    public ArtistUser addArtist(UserSession userSession, String name, String birthdateStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date birthdate;
            try {
                birthdate = dateFormat.parse(birthdateStr);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid birthdate format. Please use yyyy-MM-dd.");
            }

            ArtistUser artist = (ArtistUser) CreatorUserFactory.createCreatorUser("artist", name, birthdate);

            return artistUserRepository.save(artist);
        }
        throw new SecurityException("Only admin can add an artist");
    }

    public ArtistUser save(ArtistUser artistUser) {
        return artistUserRepository.save(artistUser);
    }

    public ArtistUser findByName(String name) {
        return artistUserRepository.findByName(name).stream().findFirst().orElse(null);
    }

    public List<ArtistUser> findAll(UserSession userSession) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            return artistUserRepository.findAll();
        }
        throw new SecurityException("Only admin can list all artists");
    }

    public Optional<ArtistUser> findById(Long id) {
        return artistUserRepository.findById(id);
    }

    public List<NormalUser> getFollowers(UserSession userSession, String artistIdStr) throws NumberFormatException {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            Long artistId = Long.parseLong(artistIdStr);
            Optional<ArtistUser> artistUserOptional = findById(artistId);
            if (artistUserOptional.isPresent()) {
                ArtistUser artist = artistUserOptional.get();
                return artist.getFollowers();
            }
            throw new EntityNotFoundException("User with specified id not found");
        }
        throw new SecurityException("Only admin can list all followers");
    }

    @Transactional
    public void delete(UserSession userSession, String idStr) throws NumberFormatException {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            Long id = Long.parseLong(idStr);
            Optional<ArtistUser> optional = artistUserRepository.findById(id);
            if (optional.isPresent()) {
                ArtistUser artist = optional.get();
                // remove the link between the artist and label
                artist.getLabel().getArtists().remove(artist);
                // remove songs associated with the artist
                for (Song song : artist.getSongs()) {
                    songService.delete(song.getId().toString());
                }
                // remove albums associated with the artist
                for (Album album : artist.getAlbums()) {
                    albumService.delete(userSession, album.getId().toString());
                }
                artistUserRepository.deleteById(id);
            }
            throw new EntityNotFoundException("Artist was not found");
        }
        throw new SecurityException("Only admin can delete an artist");
    }
}
