package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.model.Album;
import map.project.musiclibrary.data.model.Song;
import map.project.musiclibrary.service.AlbumBuilder;
import map.project.musiclibrary.service.AlbumService;
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

    @Autowired
    public AlbumCLICommands(AlbumService albumService, SongService songService) {
        this.albumService = albumService;
        this.songService = songService;
    }

    @ShellMethod(key = "listAlbums", value = "List all albums")
    public String listAlbums() {
        return albumService.findAll().toString();
    }

    @ShellMethod(key = "addAlbum", value = "Add an album")
    public String addAlbum(@ShellOption(value = {"name"}, help = "Name of the album") final String name,
                           @ShellOption(value = {"songIds"}, help = "List of song ids (format: 1,2,3)") final String songIdsStr) {
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
    }

    @ShellMethod(key = "findAlbum", value = "Find an album by name")
    public String findAlbum(@ShellOption(value = {"name"}, help = "Name of the album") final String name) {
        return albumService.findByName(name).toString();
    }
}
