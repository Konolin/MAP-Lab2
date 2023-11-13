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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
           try {
              List<Long> songIds = Arrays.stream(songIdsStr.split(","))
                      .map(Long::parseLong)
                      .collect(Collectors.toList());

              Album album = new AlbumBuilder()
                      .setName(name)
                      .setSongIds(songIds)
                      .build(songService, albumService);

              return albumService.save(album).toString();
          } catch (NumberFormatException e) {
              return "Error: Invalid integer format. Please provide valid numbers for song IDs.";
          } catch (IllegalArgumentException e) {
              return e.getMessage();
          }
        } else {
            throw return "Only admin can add an album";
        }
    }

    @ShellMethod(key = "findAlbum", value = "Find an album by name")
    public String findAlbum(@ShellOption(value = {"name"}, help = "Name of the album") final String name) {
        return albumService.findByName(name).toString();
    }
}
