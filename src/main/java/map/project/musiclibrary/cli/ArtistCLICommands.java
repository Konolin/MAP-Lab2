package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.model.Admin;
import map.project.musiclibrary.data.model.ArtistUser;
import map.project.musiclibrary.data.model.UserSession;
import map.project.musiclibrary.service.ArtistUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

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
            throw new RuntimeException("Only admin can list all artists");
        }
    }

    @ShellMethod(key = "addArtist", value = "Add an artist")
    public String addArtist(@ShellOption(value = {"name"}, help = "Name of the artist") final String name,
                            @ShellOption(value = {"birthdate"}, help = "Birthdate of the artist") final String birthdateStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            ArtistUser artist = new ArtistUser();

            artist.setName(name);
            artist.setSongs(new ArrayList<>());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date birthdate = dateFormat.parse(birthdateStr);
                artist.setBirthdate(birthdate);
            } catch (ParseException e) {
                return "Error: Invalid birthdate format. Please use yyyy-MM-dd.";
            }

            return artistUserService.save(artist).toString();
        } else {
            return "Only admin can add an artist";
        }
    }

    @ShellMethod(key = "findArtist", value = "Find an artist by name")
    public String findArtist(@ShellOption(value = {"name"}, help = "Name of the artist") final String name) {
        return artistUserService.findByName(name).toString();
    }

    //TODO - only admin can see all followers of an artist
    //TODO - de ce apare acelasi user de mai multe ori cand dau follow la un artist :((
    @ShellMethod(key = "listFollowers", value = "List the followers of an artist")
    public String getFollowers(@ShellOption(value = {"artistId"}, help = "ID of the artist") final String artistIdStr) {
        try {
            Long artistId = Long.parseLong(artistIdStr);
            Optional<ArtistUser> artistUserOptional = artistUserService.findById(artistId);
            if (artistUserOptional.isPresent()) {
                ArtistUser artist = artistUserOptional.get();
                return artist.getFollowers().toString();
            } else {
                return "Error: Artist with ID " + artistId + " not found.";
            }
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        }
    }

}
