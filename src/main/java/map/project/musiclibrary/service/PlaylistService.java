package map.project.musiclibrary.service;

import map.project.musiclibrary.data.model.audios.Playlist;
import map.project.musiclibrary.data.model.audios.Song;
import map.project.musiclibrary.data.model.users.NormalUser;
import map.project.musiclibrary.data.model.users.User;
import map.project.musiclibrary.data.repository.PlaylistRepository;
import map.project.musiclibrary.data.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public Playlist addPlaylist(String name, NormalUser currentUser) {
        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setUser(currentUser);
        playlist.setSongs(new ArrayList<>());
        return playlistRepository.save(playlist);
    }

    @Transactional
    public boolean deletePlaylist(Long id, NormalUser currentUser) {
        Optional<Playlist> playlistOptional = playlistRepository.findById(id);

        if (playlistOptional.isPresent()) {
            Playlist playlist = playlistOptional.get();

            if (currentUser.equals(playlist.getNormalUser())) {  //checking if the playlist to be deleted belongs to the user that created it
                for (Song song : playlistRepository.getReferenceById(id).getSongs()) {
                    song.setPlaylist(null);
                    songRepository.save(song);
                }

                //clear the list of songs from the playlist
                playlist.getSongs().clear();
                playlistRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }

    public Playlist save(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    public Playlist findByName(String name) {
        return playlistRepository.findByName(name).stream().findFirst().orElse(null);
    }

    @Transactional
    public List<Playlist> findAll() {
        List<Playlist> playlists = playlistRepository.findAll();

        playlists.forEach(playlist -> {
            playlist.getSongs().size();
        });

        return playlists;
    }

    @Transactional
    public Playlist addSong(String songIdStr, String playListIdStr, NormalUser currentUser) throws NumberFormatException {
        Long songId = Long.parseLong(songIdStr);
        Long playListId = Long.parseLong(playListIdStr);

        Optional<Song> songOptional = songRepository.findById(songId);
        Optional<Playlist> playlistOptional = playlistRepository.findById(playListId);

        if (songOptional.isPresent() && playlistOptional.isPresent()) {
            Song song = songOptional.get();
            Playlist playlist = playlistOptional.get();

            //associate the playlist with the logged-in user
            playlist.setUser(currentUser);
            playlist.addSong(song);
            song.setPlaylist(playlist);
            songRepository.save(song);
            return playlistRepository.save(playlist);
        }

        throw new RuntimeException("PlaylistService::Song or Playlist with specified id doesn't exist");
    }

    @Transactional
    public List<Playlist> findByUser(User user) {
        List<Playlist> playlists = playlistRepository.findByNormalUser((NormalUser) user);
        playlists.forEach(playlist -> {
            playlist.getSongs().size();
        });

        return playlists;
    }
}
