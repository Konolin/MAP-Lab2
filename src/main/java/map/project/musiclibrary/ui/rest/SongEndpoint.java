package map.project.musiclibrary.ui.rest;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.dto.SongDTO;
import map.project.musiclibrary.data.model.users.NormalUser;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/songs")
public class SongEndpoint {
    private final SongService songService;
    private final UserSession userSession;

    @Autowired
    public SongEndpoint(SongService songService, UserSession userSession) {
        this.songService = songService;
        this.userSession = userSession;
    }

    @PostMapping("/list")
    public String listSongs() {
        if (userSession.isLoggedIn()) {
            return songService.findAll().toString();
        } else {
            return "You must be logged in to se all songs";
        }
    }

    @PostMapping("/add")
    public String addSong(@RequestBody SongDTO request) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            try {
                return songService.addSong(request.getName(), request.getGenre(), request.getLengthStr(), request.getReleaseDateStr(), request.getArtistIdStr()).toString();
            } catch (IllegalArgumentException e) {
                return e.getMessage();
            } catch (Exception e) {
                return "Invalid birthdate format. Please use yyyy-MM-dd.";
            }
        } else {
            return "Only admin can add songs";
        }
    }

    @PostMapping("/find")
    public String findSong(@RequestParam String name) {
        return songService.findByName(name).toString();
    }

    @PostMapping("/delete")
    public String deleteSong(@RequestParam String idStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            try {
                songService.delete(idStr);
                return "Song successfully deleted.";
            } catch (NumberFormatException e) {
                return "Invalid id format";
            } catch (EntityNotFoundException e) {
                return "Song was not found";
            }
        } else {
            return "Only admin can delete a song";
        }
    }

    @PostMapping("/play")
    public String playSong(@RequestParam String songName) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isNormalUser()) {
            return songService.playSong(songName, (NormalUser) userSession.getCurrentUser());
        }
        return "Only normal users can play songs";
    }
}
