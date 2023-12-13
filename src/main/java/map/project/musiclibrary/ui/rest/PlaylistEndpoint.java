package map.project.musiclibrary.ui.rest;

import map.project.musiclibrary.data.model.users.NormalUser;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/playlists")
public class PlaylistEndpoint {
    private final PlaylistService playlistService;
    private final UserSession userSession;

    @Autowired
    public PlaylistEndpoint(PlaylistService playlistService, UserSession userSession) {
        this.playlistService = playlistService;
        this.userSession = userSession;
    }

    @PostMapping("/list")
    public String listPlaylists() {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isNormalUser()) {
            return playlistService.findByUser(userSession.getCurrentUser()).toString();
        } else {
            return "You must log in into a normal user account to see your playlists.";
        }
    }

    @PostMapping("/add")
    public String addPlaylist(@RequestParam String name) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isNormalUser()) {
            return playlistService.addPlaylist(name, (NormalUser) userSession.getCurrentUser()).toString();
        } else {
            return "You must log into a normal user account to add a playlist.";
        }
    }

    @PostMapping("/delete")
    public String deletePlaylist(@RequestParam String playlistIdStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isNormalUser()) {
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

    @PostMapping("/update")
    public String updatePlaylist(@RequestParam String id) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isNormalUser()) {
            try {
                Long playListId = Long.parseLong(id);
                return playlistService.updatePlaylistName(playListId);
            } catch (NumberFormatException e) {
                return "Error: Invalid user ID format. Please provide a valid number.";
            }
        } else {
            return "Only normal users can modify their playlist's name";
        }
    }

    @PostMapping("/addSong")
    public String addSongToPlaylist(@RequestParam String playlistIdStr, @RequestParam String songIdStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isNormalUser()) {
            try {
                return playlistService.addSong(songIdStr, playlistIdStr, (NormalUser) userSession.getCurrentUser()).toString();
            } catch (NumberFormatException e) {
                return "Error: Invalid integer format. Please provide a valid number.";
            } catch (RuntimeException e) {
                return "Song or Playlist with specified id doesn't exist";
            }
        } else {
            return "You must log in as a normal user to add a song to a playlist.";
        }
    }

    @PostMapping("/removeSong")
    public String removeSongFromPlaylist(@RequestParam String playlistIdStr, @RequestParam String songIdStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isNormalUser()) {
            try {
                Long playlistId = Long.parseLong(playlistIdStr);
                Long songId = Long.parseLong(songIdStr);
                return playlistService.removeSong(playlistId, songId).toString();
            } catch (NumberFormatException e) {
                return "Error: Invalid integer format. Please provide a valid number.";
            }
        } else {
            return "You must log in as a normal user to remove a song from a playlist.";
        }
    }
}
