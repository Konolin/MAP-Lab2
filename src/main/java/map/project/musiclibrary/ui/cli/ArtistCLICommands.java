package map.project.musiclibrary.ui.cli;

import jakarta.persistence.EntityNotFoundException;
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
        try {
            return artistUserService.findAll(userSession).toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "addArtist", value = "Add an artist")
    public String addArtist(@ShellOption(value = {"name"}, help = "Name of the artist") final String name,
                            @ShellOption(value = {"birthdate"}, help = "Birthdate of the artist") final String birthdateStr) {
        try {
            return artistUserService.addArtist(userSession, name, birthdateStr).toString();
        } catch (SecurityException | IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "findArtist", value = "Find an artist by name")
    public String findArtist(@ShellOption(value = {"name"}, help = "Name of the artist") final String name) {
        return artistUserService.findByName(name).toString();
    }

    @ShellMethod(key = "listFollowers", value = "List the followers of an artist")
    public String getFollowers(@ShellOption(value = {"artistId"}, help = "ID of the artist") final String artistIdStr) {
        try {
            return artistUserService.getFollowers(userSession, artistIdStr).toString();
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        } catch (SecurityException | EntityNotFoundException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "deleteArtist", value = "Delete an artist by id (It also deletes their songs!)")
    public String deleteAlbum(@ShellOption(value = {"id"}, help = "Id of the artist") final String idStr) {
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
