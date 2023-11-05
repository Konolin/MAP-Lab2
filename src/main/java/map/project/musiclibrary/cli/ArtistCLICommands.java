package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.model.ArtistUser;
import map.project.musiclibrary.service.ArtistUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@ShellComponent
public class ArtistCLICommands {
    private final ArtistUserService artistUserService;

    @Autowired
    public ArtistCLICommands(ArtistUserService artistUserService) {
        this.artistUserService = artistUserService;
    }

    @ShellMethod(key = "listArtists", value = "List all artists")
    public String listArtists() {
        return artistUserService.findAll().toString();
    }

    @ShellMethod(key = "addArtist", value = "Add an artist")
    public String addArtist(@ShellOption(value = {"name"}, help = "Name of the artist") final String name,
                            @ShellOption(value = {"email"}, help = "Email of the artist") final String email,
                            @ShellOption(value = {"birthdate"}, help = "Birthdate of the artist") final String birthdateStr) {
        ArtistUser artist = new ArtistUser();

        artist.setName(name);
        artist.setEmail(email);
        artist.setSongs(new ArrayList<>());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date birthdate = dateFormat.parse(birthdateStr);
            artist.setBirthdate(birthdate);
        } catch (ParseException e) {
            return "Error: Invalid birthdate format. Please use yyyy-MM-dd.";
        }

        return artistUserService.save(artist).toString();
    }

    @ShellMethod(key = "findArtist", value = "Find an artist by name")
    public String findArtist(@ShellOption(value = {"name"}, help = "Name of the artist") final String name) {
        return artistUserService.findByName(name).toString();
    }
}
