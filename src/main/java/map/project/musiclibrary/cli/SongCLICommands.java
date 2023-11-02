package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.repository.model.Song;
import map.project.musiclibrary.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ShellComponent
public class SongCLICommands {
    private SongService songService;

    @Autowired
    public SongCLICommands(SongService songService) {
        this.songService = songService;
    }

    @ShellMethod(key = "listSongs", value = "List all songs")
    public String listSongs() {
        return songService.findAll().toString();
    }

    @ShellMethod(key = "addSong", value = "Add a song")
    public String addSong(@ShellOption(value = {"name"}, help = "Name of the song") final String name,
                          @ShellOption(value = {"genre"}, help = "Genre of the song") final String genre,
                          @ShellOption(value = {"length"}, help = "Length of the song(in seconds)") final String lengthStr,
                          @ShellOption(value = {"releaseDate"}, help = "The release date of the song") final String releaseDateStr) {
        Song song = new Song();

        song.setName(name);
        song.setGenre(genre);
        try {
            int length = Integer.parseInt(lengthStr);
            song.setLength(length);
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date releaseDate = dateFormat.parse(releaseDateStr);
            song.setReleaseDate(releaseDate);
        } catch (ParseException e) {
            return "Error: Invalid birthdate format. Please use yyyy-MM-dd.";
        }
        //TODO - add song to album
//        song.setAlbum();

        return songService.save(song).toString();
    }

    @ShellMethod(key = "findSong", value = "Find a song by name")
    public String findSong(@ShellOption(value = {"name"}, help = "Name of the song") final String name) {
        return songService.findByName(name).toString();
    }
}
