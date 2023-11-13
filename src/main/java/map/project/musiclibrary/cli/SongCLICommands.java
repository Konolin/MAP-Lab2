package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.model.*;
import map.project.musiclibrary.service.ArtistUserService;
import map.project.musiclibrary.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@ShellComponent
public class SongCLICommands {
    private final SongService songService;
    private final ArtistUserService artistUserService;
    private final UserSession userSession;

    @Autowired
    public SongCLICommands(SongService songService, ArtistUserService artistUserService, UserSession userSession) {
        this.songService = songService;
        this.artistUserService = artistUserService;
        this.userSession = userSession;
    }

    @ShellMethod(key = "listSongs", value = "List all songs")
    public String listSongs() {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            return songService.findAll().toString();
        } else {
            throw new RuntimeException("Only admin can list all songs");
        }
    }

    //TODO - check for a user before attempting to add a song
    @ShellMethod(key = "addSong", value = "Add a song")
    public String addSong(@ShellOption(value = {"name"}, help = "Name of the song") final String name,
                          @ShellOption(value = {"genre"}, help = "Genre of the song") final String genre,
                          @ShellOption(value = {"length"}, help = "Length of the song(in seconds)") final String lengthStr,
                          @ShellOption(value = {"releaseDate"}, help = "The release date of the song") final String releaseDateStr,
                          @ShellOption(value = {"artistId"}, help = "The id of the artist of the song") final String artistIdStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
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

            try {
                // search artist by id
                Long artistId = Long.parseLong(artistIdStr);
                Optional<ArtistUser> artistUserOptional = artistUserService.findById(artistId);
                if (artistUserOptional.isPresent()) {
                    // add artist to song and add song to artists list
                    song.setArtist(artistUserOptional.get());
                    artistUserOptional.get().addSong(song);
                } else {
                    return "Error: An artist with that id does not exist";
                }
            } catch (NumberFormatException e) {
                return "Error: Invalid integer format. Please provide a valid number.";
            }

            return songService.save(song).toString();
        } else {
            return "Only admin can add songs";
        }
    }

    @ShellMethod(key = "findSong", value = "Find a song by name")
    public String findSong(@ShellOption(value = {"name"}, help = "Name of the song") final String name) {
        return songService.findByName(name).toString();
    }

    //TODO - de structurat mai bine metodele (de ex sa fie clar ce am nevoie in service/CLI ca sa mentin encapsularea)
    //TODO - cand apare un Ad, nu se poate identifica ce nume are pentru ca e null for now
    @ShellMethod(key = "playSong", value = "Play a song by ID")
    public void playSong(@ShellOption(value = {"songId"}, help = "ID of the song") final String songIdstr) {
        if (!userSession.isLoggedIn()) {
            throw new RuntimeException("You must log in to play a song.");
        }
        Long songId = Long.parseLong(songIdstr);
        if (userSession.getCurrentUser() instanceof NormalUser) {
            songService.playSong(songId, (NormalUser) userSession.getCurrentUser());
        } else {
            throw new RuntimeException("Only normal users can play songs");
        }
    }
}
