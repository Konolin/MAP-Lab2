package map.project.musiclibrary.ui.rest;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.dto.SongDTO;
import map.project.musiclibrary.data.model.users.NormalUser;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/songs")
public class SongEndpoint {
    private final SongService songService;

    @Autowired
    public SongEndpoint(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/list")
    public String listSongs() {
        try {
            return songService.findAll().toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/add")
    public String addSong(@RequestBody SongDTO request) {
        try {
            return songService.addSong(request.getName(), request.getGenre(), request.getLengthStr(), request.getReleaseDateStr(), request.getArtistIdStr()).toString();
        } catch (SecurityException | NumberFormatException | EntityNotFoundException e) {
            return e.getMessage();
        } catch (ParseException e) {
            return "Invalid birthdate format. Please use yyyy-MM-dd.";
        }
    }

    @GetMapping("/find")
    public String findSong(@RequestParam String name) {
        return songService.findByName(name).toString();
    }

    @DeleteMapping("/delete")
    public String deleteSong(@RequestParam String idStr) {
        try {
            songService.delete(idStr);
            return "Song successfully deleted.";
        } catch (NumberFormatException e) {
            return "Invalid id format";
        } catch (SecurityException | EntityNotFoundException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/play")
    public String playSong(@RequestParam String songName) {
        try {
            return songService.playSong(songName);
        } catch (SecurityException | EntityNotFoundException e) {
            return e.getMessage();
        }
    }
}
