package map.project.musiclibrary.service;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.audios.Album;
import map.project.musiclibrary.data.model.audios.Song;
import map.project.musiclibrary.data.model.users.ArtistUser;
import map.project.musiclibrary.data.model.users.NormalUser;
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

    public ArtistUser addArtist(String name, String birthdateStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthdate = dateFormat.parse(birthdateStr);

        ArtistUser artist = (ArtistUser) CreatorUserFactory.createCreatorUser("artist", name, birthdate);

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

    @Transactional
    public void delete(String idStr) throws NumberFormatException {
        Long id = Long.parseLong(idStr);
        Optional<ArtistUser> optional = artistUserRepository.findById(id);
        if (optional.isPresent()) {
            ArtistUser artist = optional.get();

            // remove the link between the artist and label
            artist.getLabel().getArtists().remove(artist);

            // remove songs associated with the artist
            // TODO - dependenta circulara fml
            for (Song song : artist.getSongs()) {
                songService.delete(song.getId().toString());
            }

            // remove albums associated with the artist
            for (Album album : artist.getAlbums()) {
                albumService.delete(album.getId().toString());
            }

            artistUserRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Artist was not found");
        }
    }
}
