package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.model.Playlist;
import map.project.musiclibrary.data.model.UserSession;
import map.project.musiclibrary.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.ArrayList;

@ShellComponent
public class PlaylistCLICommands {
    private final PlaylistService playlistService;
    private final UserSession userSession;

    @Autowired
    public PlaylistCLICommands(PlaylistService playlistService, UserSession userSession) {
        this.playlistService = playlistService;
        this.userSession = userSession;
    }

    //TODO - make sure playlist list is tailored and personalized for each user individually, because in current implementation, it retrieves all playlists, but it should only retrieve playlists from the user that has sent that request. Probably an idea would be to create a list of playlists for each userSession
    @ShellMethod(key = "listPlaylists", value = "List all playlists")
    public String listPlaylists() {
        if (!userSession.isLoggedIn()) {
            throw new RuntimeException("You must log in to see your playlists.");
        }

        return playlistService.findAll().toString();
    }

    @ShellMethod(key = "addPlaylist", value = "Add a playlist")
    public String addPlaylist(@ShellOption(value = {"name"}, help = "Name of the playlist to be added") final String name) {
        if (!userSession.isLoggedIn()) {
            throw new RuntimeException("You must log in to add a playlist.");
        }

        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setUser(userSession.getCurrentUser());
        playlist.setSongs(new ArrayList<>());
        return playlistService.save(playlist).toString();
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
