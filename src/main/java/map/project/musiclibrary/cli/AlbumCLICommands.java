package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.repository.model.Album;
import map.project.musiclibrary.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class AlbumCLICommands {
    private final AlbumService albumService;

    @Autowired
    public AlbumCLICommands(AlbumService albumService) {
        this.albumService = albumService;
    }

    @ShellMethod(key = "listAlbums", value = "List all albums")
    public String listAlbums() {
        return albumService.findAll().toString();
    }

    @ShellMethod
    public String addAlbum(@ShellOption(value = {"name"}, help = "Name of the album") final String name) {
        Album album = new Album();

        album.setName(name);
        // TODO - add artist si songs
//        album.setArtist();
//        album.setSongs();

        return albumService.save(album).toString();
    }

    @ShellMethod(key = "findAlbum", value = "Find an album by name")
    public String findAlbum(@ShellOption(value = {"name"}, help = "Name of the album") final String name) {
        return albumService.findByName(name).toString();
    }
}
