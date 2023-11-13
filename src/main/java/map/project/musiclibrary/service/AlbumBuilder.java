package map.project.musiclibrary.service;

import map.project.musiclibrary.data.model.Album;
import map.project.musiclibrary.data.model.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlbumBuilder {
    private String name;
    private List<Long> songIds;

    public AlbumBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public AlbumBuilder setSongIds(List<Long> songIds) {
        this.songIds = songIds;
        return this;
    }

    public Album build(SongService songService,AlbumService albumService) {
        Album album = new Album();
        album.setName(name);
        albumService.save(album);

        // Fetch songs based on the provided IDs
        List<Song> albumSongs = new ArrayList<>();
        for (Long songId : songIds) {
            Optional<Song> songOptional = songService.findById(songId);
            if (songOptional.isPresent()) {
                // Add song to album
                albumSongs.add(songOptional.get());
                // Set album on the song and save changes
                songOptional.get().setAlbum(album);
                songService.save(songOptional.get());
            } else {
                throw new IllegalArgumentException("Error: A song with id " + songId + " does not exist");
            }
        }
        album.setSongs(albumSongs);

        return album;
    }
}

