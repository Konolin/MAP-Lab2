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
        try {
            return songService.findAll().toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "addSong", value = "Add a song")
    public String addSong(@ShellOption(value = {"name"}, help = "Name of the song") final String name,
                          @ShellOption(value = {"genre"}, help = "Genre of the song") final String genre,
                          @ShellOption(value = {"length"}, help = "Length of the song(in seconds)") final String lengthStr,
                          @ShellOption(value = {"releaseDate"}, help = "The release date of the song") final String releaseDateStr,
                          @ShellOption(value = {"artistId"}, help = "The id of the artist of the song") final String artistIdStr) {
            try {
                return songService.addSong(name, genre, lengthStr, releaseDateStr, artistIdStr).toString();
            } catch (SecurityException | NumberFormatException | EntityNotFoundException e) {
                return e.getMessage();
            } catch (ParseException e) {
                return "Invalid birthdate format. Please use yyyy-MM-dd.";
            }
    }

    @ShellMethod(key = "findSong", value = "Find a song by name")
    public String findSong(@ShellOption(value = {"name"}, help = "Name of the song") final String name) {
        return songService.findByName(name).toString();
    }

    @ShellMethod(key = "playSong", value = "Play a song by name")
    public String playSong(@ShellOption(value = {"name"}, help = "Name of the song") final String songName) {
        try {
            return songService.playSong(songName);
        } catch (SecurityException | EntityNotFoundException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "deleteSong", value = "Delete a song by id")
    public String deleteSong(@ShellOption(value = {"id"}, help = "Id of the song") final String idStr) {
        try {
            songService.delete(idStr);
            return "Song successfully deleted.";
        } catch (NumberFormatException e) {
            return "Invalid id format";
        } catch (SecurityException | EntityNotFoundException e) {
            return e.getMessage();
        }
    }
}
