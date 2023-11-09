package map.project.musiclibrary.cli;

import map.project.musiclibrary.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

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

    @ShellMethod(key = "addSongToPlaylist", value = "Add a song to a playlist")
    public String addSongToLabel(@ShellOption (value = {"songId"}, help = "Id of the song") final String songIdstr,
                                 @ShellOption (value = {"playListId"}, help = "Id of the playlist") final String playListIdstr) {
        try{
            Long songId = Long.parseLong(songIdstr);
            Long playListId = Long.parseLong(playListIdstr);
            return playlistService.addSong(songId, playListId).toString();
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        }
    }
}
