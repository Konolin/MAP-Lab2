package map.project.musiclibrary.service;

import map.project.musiclibrary.data.model.users.NormalUser;
import map.project.musiclibrary.data.model.strategies.PlayableWithAds;
import map.project.musiclibrary.data.model.strategies.PlayableWithoutAds;
import map.project.musiclibrary.data.model.audios.Song;
import map.project.musiclibrary.data.repository.AdvertisementRepository;
import map.project.musiclibrary.data.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SongService {
    private final SongRepository songRepository;
    private final AdvertisementRepository advertisementRepository;

    @Autowired
    public SongService(SongRepository songRepository, AdvertisementRepository advertisementRepository) {
        this.songRepository = songRepository;
        this.advertisementRepository = advertisementRepository;
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

    public String playSong(String songIdStr, NormalUser currentUser) {
        try {
            Long songId = Long.parseLong(songIdStr);
            Optional<Song> songOptional = songRepository.findById(songId);
            if (songOptional.isPresent()) {
                Song song = songOptional.get();
                return song.play(currentUser.isPremium() ? new PlayableWithoutAds() : new PlayableWithAds(advertisementRepository));
            }
            return "Song not found";
        } catch (NumberFormatException e) {
            return "Invalid id";
        }
    }
}
