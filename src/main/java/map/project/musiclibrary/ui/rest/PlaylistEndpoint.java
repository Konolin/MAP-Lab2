package map.project.musiclibrary.ui.rest;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.users.NormalUser;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/playlists")
public class PlaylistEndpoint {
    private final PlaylistService playlistService;

    @Autowired
    public PlaylistEndpoint(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("/list")
    public String listPlaylists() {
        try {
            return playlistService.findByUser(UserSession.getCurrentUser()).toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/add")
    public String addPlaylist(@RequestParam String name) {
        try {
            return playlistService.addPlaylist(name, (NormalUser) UserSession.getCurrentUser()).toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @DeleteMapping("/delete")
    public String deletePlaylist(@RequestParam String playlistId) {
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

    @PutMapping("/update")
    public String updatePlaylist(@RequestParam String id) {
        try {
            Long playListId = Long.parseLong(id);
            return playlistService.updatePlaylistName(playListId);
        } catch (NumberFormatException e) {
            return "Error: Invalid user ID format. Please provide a valid number.";
        } catch (SecurityException | IllegalArgumentException | EntityNotFoundException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/addSong")
    public String addSongToPlaylist(@RequestParam String playlistIdStr, @RequestParam String songIdStr) {
        try {
            return playlistService.addSong(songIdStr, playlistIdStr, (NormalUser) UserSession.getCurrentUser()).toString();
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        } catch (EntityNotFoundException | SecurityException e) {
            return e.getMessage();
        }
    }

    @DeleteMapping("/removeSong")
    public String removeSongFromPlaylist(@RequestParam String playlistIdStr, @RequestParam String songIdStr) {
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
