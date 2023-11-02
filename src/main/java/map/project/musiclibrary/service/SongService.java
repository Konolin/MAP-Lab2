package map.project.musiclibrary.service;

import map.project.musiclibrary.data.repository.SongRepository;
import map.project.musiclibrary.data.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {
    private final SongRepository songRepository;

    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
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
}
