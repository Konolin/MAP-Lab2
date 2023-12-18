package map.project.musiclibrary.service;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.audios.Playlist;
import map.project.musiclibrary.data.model.audios.Song;
import map.project.musiclibrary.data.model.strategies.PlayableWithAds;
import map.project.musiclibrary.data.model.strategies.PlayableWithoutAds;
import map.project.musiclibrary.data.model.users.ArtistUser;
import map.project.musiclibrary.data.model.users.NormalUser;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.data.repository.AdvertisementRepository;
import map.project.musiclibrary.data.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SongService {
    private final SongRepository songRepository;
    private final AdvertisementRepository advertisementRepository;
    private final ArtistUserService artistUserService;

    @Autowired
    public SongService(SongRepository songRepository, AdvertisementRepository advertisementRepository, ArtistUserService artistUserService) {
        this.songRepository = songRepository;
        this.advertisementRepository = advertisementRepository;
        this.artistUserService = artistUserService;
    }

    public Song addSong(String name, String genre, String lengthStr, String releaseDateStr, String artistIdStr) throws ParseException {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isAdmin()) {
            Song song = new Song();
            song.setName(name);
            song.setGenre(genre);

            try {
                int length = Integer.parseInt(lengthStr);
                song.setLength(length);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Invalid integer format. Please provide a valid number.");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date releaseDate = dateFormat.parse(releaseDateStr);
            song.setReleaseDate(releaseDate);

            try {
                // search artist by id
                Long artistId = Long.parseLong(artistIdStr);
                Optional<ArtistUser> artistUserOptional = artistUserService.findById(artistId);
                if (artistUserOptional.isPresent()) {
                    // add artist to song and add song to artists list
                    song.setArtist(artistUserOptional.get());
                    artistUserOptional.get().addSong(song);
                } else {
                    throw new EntityNotFoundException("An artist with that id does not exist");
                }
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Invalid integer format. Please provide a valid number.");
            }
            return songRepository.save(song);
        }
        throw new SecurityException("Only admin can add a song");
    }

    public Song save(Song song) {
        return songRepository.save(song);
    }

    public Song findByName(String name) {
        return songRepository.findByName(name).stream().findFirst().orElse(null);
    }

    public List<Song> findAll() {
        if (UserSession.isLoggedIn()) {
            return songRepository.findAll();
        }
        throw new SecurityException("You must be logged in to view all songs");
    }

    public Optional<Song> findById(Long id) {
        return songRepository.findById(id);
    }

    public String playSong(String songName) {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isNormalUser()) {
            List<Song> foundSongs = songRepository.findByName(songName);
            if (!foundSongs.isEmpty()) {
                NormalUser currentUser = (NormalUser) UserSession.getCurrentUser();
                return foundSongs.getFirst().play(currentUser.isPremium() ? new PlayableWithoutAds() : new PlayableWithAds(advertisementRepository));
            }
            throw new EntityNotFoundException("Song was not found");
        }
        throw new SecurityException("Only normal users can play songs");
    }

    public void delete(String idStr) throws NumberFormatException {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isAdmin()) {
            Long id = Long.parseLong(idStr);
            Optional<Song> optional = songRepository.findById(id);
            if (optional.isPresent()) {
                Song song = optional.get();
                song.setArtist(null);
                song.setAlbum(null);
                for (Playlist playlist : song.getPlaylists()) {
                    playlist.getSongs().remove(song);
                }
                songRepository.deleteById(id);
            } else {
                throw new EntityNotFoundException("Song was not found");
            }
        }
        throw new SecurityException("Only admin can delete a song");
    }
}
