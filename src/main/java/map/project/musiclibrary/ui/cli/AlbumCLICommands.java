package map.project.musiclibrary.ui.cli;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.transaction.annotation.Transactional;

@ShellComponent
public class AlbumCLICommands {
    private final AlbumService albumService;

    @Autowired
    public AlbumCLICommands(AlbumService albumService) {
        this.albumService = albumService;
    }

    @ShellMethod(key = "listAlbums", value = "List all albums")
    public String listAlbums() {
        try {
            return albumService.findAll().toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @Transactional
    @ShellMethod(key = "addAlbum", value = "Add an album")
    public String addAlbum(@ShellOption(value = {"name"}, help = "Name of the album") final String name,
                           @ShellOption(value = {"artistId"}, help = "ID of the artist") final String artistIdStr,
                           @ShellOption(value = {"songIds"}, help = "List of song ids (format: 1,2,3)") final String songIdsStr) {
        try {
            return albumService.addAlbum(name, artistIdStr, songIdsStr).toString();
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide valid numbers for song IDs.";
        } catch (IllegalArgumentException | SecurityException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "findAlbum", value = "Find an album by name")
    public String findAlbum(@ShellOption(value = {"name"}, help = "Name of the album") final String name) {
        return albumService.findByName(name).toString();
    }

    @ShellMethod(key = "deleteAlbum", value = "Delete an album by id")
    public String deleteAlbum(@ShellOption(value = {"id"}, help = "Id of the album") final String idStr) {
        try {
            albumService.delete(idStr);
            return "Album with ID " + idStr + " has been deleted successfully!";
        } catch (NumberFormatException e) {
            return "Invalid id format";
        } catch (EntityNotFoundException | SecurityException e) {
            return e.getMessage();
        }
    }
}
