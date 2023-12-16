package map.project.musiclibrary.ui.rest;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.dto.ArtistDTO;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.ArtistUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/artist")
public class ArtistEndpoint {
    private final ArtistUserService artistUserService;
    private final UserSession userSession;

    @Autowired
    public ArtistEndpoint(ArtistUserService artistUserService, UserSession userSession) {
        this.artistUserService = artistUserService;
        this.userSession = userSession;
    }

    @GetMapping("/list")
    public String listArtists() {
        try {
            return artistUserService.findAll(userSession).toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/add")
    public String addArtist(@RequestBody ArtistDTO request) {
        try {
            return artistUserService.addArtist(userSession, request.getName(), request.getBirthdate()).toString();
        } catch (SecurityException | IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/find")
    public String findArtist(@RequestParam String name) {
        return artistUserService.findByName(name).toString();
    }

    @GetMapping("/followers")
    public String getFollowers(@RequestParam String artistIdStr) {
        try {
            return artistUserService.getFollowers(userSession, artistIdStr).toString();
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        } catch (SecurityException | EntityNotFoundException e) {
            return e.getMessage();
        }
    }

    @DeleteMapping("/delete")
    public String deleteArtist(@RequestParam String idStr) {
        try {
            artistUserService.delete(userSession, idStr);
            return "Artist with ID " + idStr + " has been deleted successfully!";
        } catch (NumberFormatException e) {
            return "Invalid id format";
        } catch (SecurityException | EntityNotFoundException e) {
            return e.getMessage();
        }
    }
}
