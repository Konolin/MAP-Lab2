package map.project.musiclibrary.service;

import map.project.musiclibrary.data.model.NormalUser;
import map.project.musiclibrary.data.model.Song;
import map.project.musiclibrary.data.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SongService {
    private final SongRepository songRepository;
    private final NormalUserService normalUserService;

    @Autowired
    public SongService(SongRepository songRepository, NormalUserService normalUserService) {
        this.songRepository = songRepository;
        this.normalUserService = normalUserService;
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

    public void playSong(Long songId, NormalUser currentUser) {
        Optional<Song> songOptional = songRepository.findById(songId);

        if (songOptional.isPresent()) {
            Song song = songOptional.get();
            normalUserService.playAudio(song, currentUser.isPremium());
        } else {
            throw new RuntimeException("Song not found");
        }
    }
}
