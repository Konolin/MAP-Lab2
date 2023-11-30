package map.project.musiclibrary.service;

import jakarta.persistence.EntityNotFoundException;
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

    public String updatePlaylistName(Long id){
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);

        if (optionalPlaylist.isPresent()){
            Playlist playlist = optionalPlaylist.get();
            String newPlaylistName = promptPlaylistName();
            if (playlist.getName().equals(newPlaylistName)) {
                return "Error: New playlist name can't be the same as the old name.";
            }
            playlist.setName(newPlaylistName);
            playlistRepository.save(playlist);
            return "Changes saved!";
        } else {
            throw new EntityNotFoundException("Playlist not found!");
        }
    }

    public String promptPlaylistName(){
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
        List<Playlist> playlists = playlistRepository.findAll();

        playlists.forEach(playlist -> {
            playlist.getSongs().size();
        });

        return playlists;
    }

    @Transactional
    public String addSong(String songIdStr, String playListIdStr, NormalUser currentUser) throws NumberFormatException {
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
            playlistRepository.save(playlist);
            return "Song " + song.getName() + " by " + song.getArtist().getName() + " has been added to playlist " + playlist.getName();
        } else {
            throw new EntityNotFoundException("Song or playlist not found.");
        }
    }

    @Transactional
    public String removeSong(Long playlistId, Long songId){
        Optional<Song> optionalSong = songRepository.findById(songId);
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);

        if (optionalPlaylist.isPresent() && optionalSong.isPresent()) {
            Playlist playlist = optionalPlaylist.get();
            Song song = optionalSong.get();

            playlist.removeSong(song);
            song.getPlaylists().remove(playlist);
            songRepository.save(song);
            playlistRepository.save(playlist);
            return "Song " + song.getName() + " by " + song.getArtist().getName() + " has been deleted from playlist " + playlist.getName();
        } else {
            throw new EntityNotFoundException("Song or playlist not found.");
        }
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
