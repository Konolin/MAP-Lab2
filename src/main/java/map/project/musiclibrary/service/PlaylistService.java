package map.project.musiclibrary.service;

import map.project.musiclibrary.data.model.*;
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
    private final UserSession userSession;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository, SongRepository songRepository, UserSession userSession) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
        this.userSession = userSession;
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
    public Playlist addSong(Long songId, Long playListId) {
        //check if a user is logged in
        if (!userSession.isLoggedIn()) {
            throw new RuntimeException("You must log in to add a song to a playlist.");
        }

        Optional<Song> songOptional = songRepository.findById(songId);
        Optional<Playlist> playlistOptional = playlistRepository.findById(playListId);

        if (songOptional.isPresent() && playlistOptional.isPresent()) {
            Song song = songOptional.get();
            Playlist playlist = playlistOptional.get();

            //associate the playlist with the logged-in user
            User currentUser = userSession.getCurrentUser();
            if (currentUser instanceof NormalUser) {
                playlist.setUser((NormalUser) currentUser);
            } else {
                //handle the case where the current user is not a NormalUser (de ex admin)
                throw new RuntimeException("Only normal users can add songs to playlists.");
            }

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
