package map.project.musiclibrary.ui.rest;

import map.project.musiclibrary.data.dto.AlbumDTO;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/album")
public class AlbumEndpoint {
    private final AlbumService albumService;
    private final UserSession userSession;

    @Autowired
    public AlbumEndpoint(AlbumService albumService, UserSession userSession) {
        this.albumService = albumService;
        this.userSession = userSession;
    }

    @PostMapping("/list")
    public String listAlbums() {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            return albumService.findAll().toString();
        } else {
            return "Only admins can list all albums";
        }
    }

    @PostMapping("/add")
    public String addAlbum(@RequestBody AlbumDTO request) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            try {
                return albumService.addAlbum(request.getName(), request.getArtistId(), request.getSongIds()).toString();
            } catch (NumberFormatException e) {
                return "Error: Invalid integer format. Please provide valid numbers for song IDs.";
            } catch (IllegalArgumentException e) {
                return e.getMessage();
            }
        } else {
            return "Only admins can add an album";
        }
    }

    @PostMapping("/find")
    public String findAlbum(@RequestParam String name) {
        return albumService.findByName(name).toString();
    }

    @PostMapping("/delete")
    public String deleteAlbum(@RequestParam String idStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            try {
                albumService.delete(idStr);
                return "Album with ID " + idStr + " has been deleted successfully!";
            } catch (NumberFormatException e) {
                return "Error: Invalid integer format. Please provide a valid number.";
            }
        } else {
            return "Only admins can delete albums.";
        }
    }
}
