package map.project.musiclibrary.service;

import map.project.musiclibrary.data.model.Playlist;
import map.project.musiclibrary.data.model.Song;
import map.project.musiclibrary.data.repository.PlaylistRepository;
import map.project.musiclibrary.data.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository, SongRepository songRepository) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
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

    @Transactional
    public Playlist addSong(Long songId, Long playListId){
        Optional<Song> songOptional = songRepository.findById(songId);
        Optional<Playlist> playlistOptional = playlistRepository.findById(playListId);

        if (songOptional.isPresent() && playlistOptional.isPresent()) {
            Song song = songOptional.get();
            Playlist playlist = playlistOptional.get();
            playlist.addSong(song);
            song.setPlaylist(playlist);
            songRepository.save(song);
            return playlistRepository.save(playlist);
        }
        throw new RuntimeException("PlaylistService::Song or Playlist with specified id doesn't exist");
    }
}
