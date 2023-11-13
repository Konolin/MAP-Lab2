package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.model.*;
import map.project.musiclibrary.service.AlbumService;
import map.project.musiclibrary.service.ArtistUserService;
import map.project.musiclibrary.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ShellComponent
public class AlbumCLICommands {
    private final AlbumService albumService;
    private final SongService songService;
    private final UserSession userSession;
    private final ArtistUserService artistUserService;

    @Autowired
    public AlbumCLICommands(AlbumService albumService, SongService songService, UserSession userSession, ArtistUserService artistUserService) {
        this.albumService = albumService;
        this.songService = songService;
        this.userSession = userSession;
        this.artistUserService = artistUserService;
    }

    @ShellMethod(key = "listAlbums", value = "List all albums")
    public String listAlbums() {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            return albumService.findAll().toString();
        } else {
            throw new RuntimeException("Only admin can list all albums");  //tbh asta e discutabil, depinde de preferinte
        }
    }

    @ShellMethod(key = "addAlbum", value = "Add an album")
    public String addAlbum(@ShellOption(value = {"name"}, help = "Name of the album") final String name,
                           @ShellOption(value = {"artistId"}, help = "ID of the artist") final String artistIdStr,
                           @ShellOption(value = {"songIds"}, help = "List of song ids (format: 1,2,3)") final String songIdsStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            Album album = new Album();

            album.setName(name);
            albumService.save(album); // save album to generate an id (nu salveaza melodiile fara asta)

            // Split the input string of songIds into a list
            String[] songIdList = songIdsStr.split(",");
            // Create and add Song objects to the album
            List<Song> albumSongs = new ArrayList<>();
            for (String songIdStr : songIdList) {
                try {
                    // search song by id
                    Long songId = Long.parseLong(songIdStr);
                    Optional<Song> songOptional = songService.findById(songId);
                    if (songOptional.isPresent()) {
                        // add song to songList
                        albumSongs.add(songOptional.get());
                        // add album to song and save changes
                        songOptional.get().setAlbum(album);
                        songService.save(songOptional.get());
                    } else {
                        return "Error: A song with that id does not exist";
                    }
                } catch (NumberFormatException e) {
                    return "Error: Invalid integer format. Please provide a valid number.";
                }
            }
            album.setSongs(albumSongs);
            if (album.getArtist() != null) {
                try {
                    Long artistId = Long.parseLong(artistIdStr);
                    artistUserService.releaseAlbum(artistId, album);
                    artistUserService.save(album.getArtist()); // save changes to the artist
                } catch(NumberFormatException e){
                    return "Error: Invalid integer format. Please provide a valid number.";
            }
        }

            return albumService.save(album).toString();
        } else {
            throw new RuntimeException("Only admin can add an album");
        }
    }

    @ShellMethod(key = "findAlbum", value = "Find an album by name")
    public String findAlbum(@ShellOption(value = {"name"}, help = "Name of the album") final String name) {
        return albumService.findByName(name).toString();
    }
}
