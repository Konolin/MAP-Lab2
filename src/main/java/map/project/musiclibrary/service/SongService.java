package map.project.musiclibrary.service;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.audios.Playlist;
import map.project.musiclibrary.data.model.audios.Song;
import map.project.musiclibrary.data.model.strategies.PlayableWithAds;
import map.project.musiclibrary.data.model.strategies.PlayableWithoutAds;
import map.project.musiclibrary.data.model.users.ArtistUser;
import map.project.musiclibrary.data.model.users.NormalUser;
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
                throw new IllegalArgumentException("An artist with that id does not exist");
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid integer format. Please provide a valid number.");
        }
        return songRepository.save(song);
    }

    public Song save(Song song) {
        return songRepository.save(song);
    }

    public Song findByName(String name) {
        return songRepository.findByName(name).stream().findFirst().orElse(null);
    }

    public List<Song> findAll() {
        return songRepository.findAll();
    }

    public Optional<Song> findById(Long id) {
        return songRepository.findById(id);
    }

    public String playSong(String songName, NormalUser currentUser) {
        List<Song> foundSongs = songRepository.findByName(songName);
        if (!foundSongs.isEmpty()) {
            return foundSongs.getFirst().play(currentUser.isPremium() ? new PlayableWithoutAds() : new PlayableWithAds(advertisementRepository));
        }
        return "Song not found";
    }

    public void delete(String idStr) throws NumberFormatException {
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
}
