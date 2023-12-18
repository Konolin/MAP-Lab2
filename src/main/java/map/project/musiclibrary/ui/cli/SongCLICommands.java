package map.project.musiclibrary.ui.cli;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.users.NormalUser;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.text.ParseException;

@ShellComponent
public class SongCLICommands {
    private final SongService songService;

    @Autowired
    public SongCLICommands(SongService songService) {
        this.songService = songService;
    }

    @ShellMethod(key = "listSongs", value = "List all songs")
    public String listSongs() {
        if (UserSession.isLoggedIn()) {
            return songService.findAll().toString();
        } else {
            return "You must be logged in to se all songs";
        }
    }

    @ShellMethod(key = "addSong", value = "Add a song")
    public String addSong(@ShellOption(value = {"name"}, help = "Name of the song") final String name,
                          @ShellOption(value = {"genre"}, help = "Genre of the song") final String genre,
                          @ShellOption(value = {"length"}, help = "Length of the song(in seconds)") final String lengthStr,
                          @ShellOption(value = {"releaseDate"}, help = "The release date of the song") final String releaseDateStr,
                          @ShellOption(value = {"artistId"}, help = "The id of the artist of the song") final String artistIdStr) {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isAdmin()) {
            try {
                return songService.addSong(name, genre, lengthStr, releaseDateStr, artistIdStr).toString();
            } catch (IllegalArgumentException e) {
                return e.getMessage();
            } catch (ParseException e) {
                return "Invalid birthdate format. Please use yyyy-MM-dd.";
            }
        } else {
            return "Only admin can add songs";
        }
    }

    @ShellMethod(key = "findSong", value = "Find a song by name")
    public String findSong(@ShellOption(value = {"name"}, help = "Name of the song") final String name) {
        return songService.findByName(name).toString();
    }

    @ShellMethod(key = "playSong", value = "Play a song by name")
    public String playSong(@ShellOption(value = {"name"}, help = "Name of the song") final String songName) {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isNormalUser()) {
            return songService.playSong(songName, (NormalUser) UserSession.getCurrentUser());
        }
        return "Only normal users can play songs";
    }

    @ShellMethod(key = "deleteSong", value = "Delete a song by id")
    public String deleteSong(@ShellOption(value = {"id"}, help = "Id of the song") final String idStr) {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isAdmin()) {
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
}
