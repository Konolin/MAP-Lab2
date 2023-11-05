package map.project.musiclibrary.service;

import map.project.musiclibrary.data.model.Playlist;
import map.project.musiclibrary.data.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public Playlist save(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    public Playlist findByName(String name) {
        return playlistRepository.findByName(name).stream().findFirst().orElse(null);
    }

    public List<Playlist> findAll() {
        return playlistRepository.findAll();
    }
}
