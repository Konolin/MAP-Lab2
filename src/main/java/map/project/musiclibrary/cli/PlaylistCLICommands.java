package map.project.musiclibrary.cli;

import map.project.musiclibrary.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class PlaylistCLICommands {
    private final PlaylistService playlistService;

    @Autowired
    public PlaylistCLICommands(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @ShellMethod(key = "listPlaylists", value = "List all playlists")
    public String listPlaylists() {
        return playlistService.findAll().toString();
    }
}
