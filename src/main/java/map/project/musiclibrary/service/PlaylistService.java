package map.project.musiclibrary.service;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.audios.Playlist;
import map.project.musiclibrary.data.model.audios.Song;
import map.project.musiclibrary.data.model.users.NormalUser;
import map.project.musiclibrary.data.model.users.User;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.data.repository.PlaylistRepository;
import map.project.musiclibrary.data.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

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
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isNormalUser()) {
            Playlist playlist = new Playlist();
            playlist.setName(name);
            playlist.setUser(currentUser);
            playlist.setSongs(new ArrayList<>());
            return playlistRepository.save(playlist);
        }
        throw new SecurityException("Only normal users can add a playlist.");
    }

    @Transactional
    public boolean deletePlaylist(String idStr) throws NumberFormatException {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isNormalUser()) {
            Long id = Long.parseLong(idStr);
            Optional<Playlist> playlistOptional = playlistRepository.findById(id);
            if (playlistOptional.isPresent()) {
                Playlist playlist = playlistOptional.get();
                if (UserSession.getCurrentUser().equals(playlist.getNormalUser())) {  //checking if the playlist to be deleted belongs to the user that created it
                    for (Song song : playlist.getSongs()) {
                        song.getPlaylists().remove(playlist);
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
        throw new SecurityException("Only normal users can delete their playlists.");
    }

    public String updatePlaylistName(Long id) {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isNormalUser()) {
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);

            if (optionalPlaylist.isPresent()) {
                Playlist playlist = optionalPlaylist.get();
                String newPlaylistName = promptPlaylistName();
                if (playlist.getName().equals(newPlaylistName)) {
                    throw new IllegalArgumentException("Error: New playlist name can't be the same as the old name.");
                }
                playlist.setName(newPlaylistName);
                playlistRepository.save(playlist);
                return "Changes saved!";
            } else {
                throw new EntityNotFoundException("Playlist not found!");
            }
        }
        throw new SecurityException("Only normal users can update their playlists.");
    }

    public String promptPlaylistName() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the new playlist name: ");
        return scanner.nextLine();
    }

    public Playlist save(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    public Playlist findByName(String name) {
        return playlistRepository.findByName(name).stream().findFirst().orElse(null);
    }

    @Transactional
    public List<Playlist> findAll() {
        return playlistRepository.findAll();
    }

    @Transactional
    public Playlist addSong(String songIdStr, String playListIdStr, NormalUser currentUser) throws NumberFormatException {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isNormalUser()) {
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
                song.getPlaylists().add(playlist);
                songRepository.save(song);
                return playlistRepository.save(playlist);
            }
            throw new EntityNotFoundException("PlaylistService::Song or Playlist with specified id doesn't exist");
        }
        throw new SecurityException("Only normal users can add a song to a playlist.");
    }

    @Transactional
    public Playlist removeSong(Long playlistId, Long songId) {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isNormalUser()) {
            Optional<Song> optionalSong = songRepository.findById(songId);
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);

            if (optionalPlaylist.isPresent() && optionalSong.isPresent()) {
                Playlist playlist = optionalPlaylist.get();
                Song song = optionalSong.get();

                playlist.removeSong(song);
                song.getPlaylists().remove(playlist);
                songRepository.save(song);
                return playlistRepository.save(playlist);
            } else {
                throw new EntityNotFoundException("Song or playlist not found.");
            }
        }
        throw new SecurityException("Only normal users can remove a song from a playlist.");
    }

    @Transactional
    public List<Playlist> findByUser(User user) {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isNormalUser()) {
            return playlistRepository.findByNormalUser((NormalUser) user);
        }
        throw new SecurityException("Only normal users can view their playlists.");
    }
}
