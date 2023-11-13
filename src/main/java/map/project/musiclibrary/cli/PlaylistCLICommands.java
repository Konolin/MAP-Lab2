package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.model.NormalUser;
import map.project.musiclibrary.data.model.Playlist;
import map.project.musiclibrary.data.model.User;
import map.project.musiclibrary.data.model.UserSession;
import map.project.musiclibrary.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
public class PlaylistCLICommands {
    private final PlaylistService playlistService;
    private final UserSession userSession;

    @Autowired
    public PlaylistCLICommands(PlaylistService playlistService, UserSession userSession) {
        this.playlistService = playlistService;
        this.userSession = userSession;
    }

    @ShellMethod(key = "listPlaylists", value = "List all playlists")
    public String listPlaylists() {
        if (!userSession.isLoggedIn()) {
            throw new RuntimeException("You must log in to see your playlists.");
        }

        User currentUser = userSession.getCurrentUser();
        if (currentUser instanceof NormalUser) {
            List<Playlist> userPlaylists = playlistService.findByUser(currentUser);
            return userPlaylists.toString();
        } else {
            throw new RuntimeException("Only normal users can see their playlists.");
        }


    }


    @ShellMethod(key = "addPlaylist", value = "Add a playlist")
    public String addPlaylist(@ShellOption(value = {"name"}, help = "Name of the playlist to be added") final String name) {
        if (!userSession.isLoggedIn()) {
            throw new RuntimeException("You must log in to add a playlist.");
        }

        Playlist playlist = new Playlist();
        playlist.setName(name);
        if (userSession.getCurrentUser() instanceof NormalUser){
            playlist.setUser((NormalUser) userSession.getCurrentUser());
            playlist.setSongs(new ArrayList<>());
            return playlistService.save(playlist).toString();
        } else {
            throw new RuntimeException("Only normal users can add a playlist.");
        }
    }


    @ShellMethod(key = "addSongToPlaylist", value = "Add a song to a playlist")
    public String addSongToPlaylist(@ShellOption (value = {"songId"}, help = "Id of the song") final String songIdstr,
                                 @ShellOption (value = {"playListId"}, help = "Id of the playlist") final String playListIdstr) {
        try{
            Long songId = Long.parseLong(songIdstr);
            Long playListId = Long.parseLong(playListIdstr);
            return playlistService.addSong(songId, playListId).toString();
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        }
    }
}
