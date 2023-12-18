package map.project.musiclibrary.ui.cli;

import jakarta.persistence.EntityNotFoundException;
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

    @Autowired
    public PlaylistCLICommands(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @ShellMethod(key = "listPlaylists", value = "List all playlists")
    public String listPlaylists() {
        try {
            return playlistService.findByUser(UserSession.getCurrentUser()).toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "addPlaylist", value = "Add a playlist")
    public String addPlaylist(@ShellOption(value = {"name"}, help = "Name of the playlist to be added") final String name) {
        try {
            return playlistService.addPlaylist(name, (NormalUser) UserSession.getCurrentUser()).toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "deletePlaylist", value = "Delete a playlist")
    public String deletePlaylist(@ShellOption(value = {"playlistId"}, help = "ID of the playlist to be deleted") final String playlistId) {
        try {
            if (playlistService.deletePlaylist(playlistId))
                return "Playlist with ID " + playlistId + " has been deleted.";
            else
                return "Playlist with ID " + playlistId + " doesn't exist or you don't have permission to delete it.";
        } catch (IllegalArgumentException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "updatePlaylist", value = "Update a playlist's name")
    public String updatePlaylist(@ShellOption(value = {"id"}, help = "ID of the playlist to be updated") final String id) {
        try {
            Long playListId = Long.parseLong(id);
            return playlistService.updatePlaylistName(playListId);
        } catch (NumberFormatException e) {
            return "Error: Invalid user ID format. Please provide a valid number.";
        } catch (SecurityException | IllegalArgumentException | EntityNotFoundException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "addSongToPlaylist", value = "Add a song to a playlist")
    public String addSongToPlaylist(@ShellOption(value = {"songId"}, help = "Id of the song") final String songIdStr,
                                    @ShellOption(value = {"playListId"}, help = "Id of the playlist") final String playListIdStr) {
        try {
            return playlistService.addSong(songIdStr, playListIdStr, (NormalUser) UserSession.getCurrentUser()).toString();
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        } catch (EntityNotFoundException | SecurityException e) {
            return e.getMessage();
        }
}

    @ShellMethod(key = "removeSongFromPlaylist", value = "Remove a song from a playlist")
    public String removeSongFromPlaylist(@ShellOption(value = {"songId"}, help = "ID of the song") final String songIdStr,
                                         @ShellOption(value = {"playlistId"}, help = "ID of the playlist") final String playlistIdStr) {
        try {
            Long playlistId = Long.parseLong(playlistIdStr);
            Long songId = Long.parseLong(songIdStr);
            return playlistService.removeSong(playlistId, songId).toString();
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        } catch (SecurityException | EntityNotFoundException e) {
            return e.getMessage();
        }
    }
}
