package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.model.users.NormalUser;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

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
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof NormalUser) {
            return playlistService.findByUser(userSession.getCurrentUser()).toString();
        } else {
            return "You must log in into a normal user account to see your playlists.";
        }
    }

    @ShellMethod(key = "addPlaylist", value = "Add a playlist")
    public String addPlaylist(@ShellOption(value = {"name"}, help = "Name of the playlist to be added") final String name) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof NormalUser) {
            return playlistService.addPlaylist(name, (NormalUser) userSession.getCurrentUser()).toString();
        } else {
            return "You must log into a normal user account to add a playlist.";
        }
    }

    @ShellMethod(key = "deletePlaylist", value = "Delete a playlist")
    public String deletePlaylist(@ShellOption(value = {"playlistId"}, help = "ID of the playlist to be deleted") final String playlistIdStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof NormalUser) {
            try {
                Long playlistId = Long.parseLong(playlistIdStr);
                NormalUser currentUser = (NormalUser) userSession.getCurrentUser();
                if (playlistService.deletePlaylist(playlistId, currentUser))
                    return "Playlist with ID " + playlistId + " has been deleted.";
                else
                    return "Playlist with ID " + playlistId + " doesn't exist or you don't have permission to delete it.";
            } catch (IllegalArgumentException e) {
                return "Error: Invalid integer format. Please provide a valid number.";
            }
        } else {
            return "Only normal users can delete a playlist.";
        }
    }

    @ShellMethod(key = "addSongToPlaylist", value = "Add a song to a playlist")
    public String addSongToPlaylist(@ShellOption(value = {"songId"}, help = "Id of the song") final String songIdStr,
                                    @ShellOption(value = {"playListId"}, help = "Id of the playlist") final String playListIdStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof NormalUser) {
            try {
                return playlistService.addSong(songIdStr, playListIdStr, (NormalUser) userSession.getCurrentUser()).toString();
            } catch (NumberFormatException e) {
                return "Error: Invalid integer format. Please provide a valid number.";
            } catch (RuntimeException e) {
                return "Song or Playlist with specified id doesn't exist";
            }
        } else {
            return "You must log in as a normal user to add a song to a playlist.";
        }
    }
}
