package map.project.musiclibrary.cli;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.users.Admin;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.ArtistUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.text.ParseException;

@ShellComponent
public class ArtistCLICommands {
    private final ArtistUserService artistUserService;
    private final UserSession userSession;

    @Autowired
    public ArtistCLICommands(ArtistUserService artistUserService, UserSession userSession) {
        this.artistUserService = artistUserService;
        this.userSession = userSession;
    }

    @ShellMethod(key = "listArtists", value = "List all artists")
    public String listArtists() {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            return artistUserService.findAll().toString();
        } else {
            return "Only admin can list all artists";
        }
    }

    @ShellMethod(key = "addArtist", value = "Add an artist")
    public String addArtist(@ShellOption(value = {"name"}, help = "Name of the artist") final String name,
                            @ShellOption(value = {"birthdate"}, help = "Birthdate of the artist") final String birthdateStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            try {
                return artistUserService.addArtist(name, birthdateStr).toString();
            } catch (ParseException e) {
                return "Error: Invalid birthdate format. Please use yyyy-MM-dd.";
            }
        } else {
            return "Only admin can add an artist";
        }
    }

    @ShellMethod(key = "findArtist", value = "Find an artist by name")
    public String findArtist(@ShellOption(value = {"name"}, help = "Name of the artist") final String name) {
        return artistUserService.findByName(name).toString();
    }

    @ShellMethod(key = "listFollowers", value = "List the followers of an artist")
    public String getFollowers(@ShellOption(value = {"artistId"}, help = "ID of the artist") final String artistIdStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            try {
                return artistUserService.getFollowers(artistIdStr).toString();
            } catch (NumberFormatException e) {
                return "Error: Invalid integer format. Please provide a valid number.";
            } catch (EntityNotFoundException e) {
                return "User with specified id not found";
            }
        } else {
            return "Only admin can list all the followers of an artist";
        }
    }

    @ShellMethod(key = "deleteArtist", value = "Delete an artist by id (It also deletes their songs!)")
    public String deleteAlbum(@ShellOption(value = {"id"}, help = "Id of the artist") final String idStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            try {
                artistUserService.delete(idStr);
                return "Artist successfully deleted.";
            } catch (NumberFormatException e) {
                return "Invalid id format";
            } catch (EntityNotFoundException e) {
                return "Artist was not found";
            }
        } else {
            return "Only admin can delete an artist";
        }
    }
}
