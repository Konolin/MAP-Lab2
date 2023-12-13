package map.project.musiclibrary.ui.rest;

import map.project.musiclibrary.data.dto.ArtistDTO;
import map.project.musiclibrary.data.model.users.Admin;
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

    @PostMapping("/list")
    public String listArtists() {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            return artistUserService.findAll().toString();
        } else {
            return "Only admin can list all artists";
        }
    }

    @PostMapping("/add")
    public String addArtist(@RequestBody ArtistDTO request) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            try {
                return artistUserService.addArtist(request.getName(), request.getBirthdate()).toString();
            } catch (ParseException e) {
                return "Error: Invalid birthdate format. Please use yyyy-MM-dd.";
            }
        } else {
            return "Only admin can add an artist";
        }
    }

    @PostMapping("/find")
    public String findArtist(@RequestParam String name) {
        return artistUserService.findByName(name).toString();
    }

    @PostMapping("/followers")
    public String getFollowers(@RequestParam String artistIdStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            try {
                return artistUserService.getFollowers(artistIdStr).toString();
            } catch (NumberFormatException e) {
                return "Error: Invalid integer format. Please provide a valid number.";
            }
        } else {
            return "Only admin can list all followers";
        }
    }

    @PostMapping("/delete")
    public String deleteArtist(@RequestParam String idStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            try {
                artistUserService.delete(idStr);
                return "Artist with ID " + idStr + " has been deleted successfully!";
            } catch (IllegalArgumentException e) {
                return "Invalid id format";
            }
        } else {
            return "Only admin can delete artists.";
        }
    }
}
