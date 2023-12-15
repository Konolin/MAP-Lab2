package map.project.musiclibrary.service;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.audios.Album;
import map.project.musiclibrary.data.model.audios.Song;
import map.project.musiclibrary.data.model.users.ArtistUser;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.data.repository.AlbumRepository;
import map.project.musiclibrary.service.builders.AlbumBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final ArtistUserService artistUserService;
    private final SongService songService;


    @Autowired
    public AlbumService(AlbumRepository albumRepository, ArtistUserService artistUserService, SongService songService) {
        this.albumRepository = albumRepository;
        this.artistUserService = artistUserService;
        this.songService = songService;
    }

    public Album addAlbum(UserSession userSession, String name, String artistIdStr, String songIdsStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            List<Long> songIds = Arrays.stream(songIdsStr.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            //retrieve artist and release album
            Long artistId = Long.parseLong(artistIdStr);
            ArtistUser artist = artistUserService.findById(artistId)
                    .orElseThrow(() -> new EntityNotFoundException("Artist with ID " + artistId + " not found."));
            Album album = new AlbumBuilder()
                    .setName(name)
                    .setSongIds(songIds)
                    .build(songService, this);

            releaseAlbum(artist, album);
            return albumRepository.save(album);
        }
        throw new SecurityException("Only admins can add albums.");
    }

    public Album save(Album album) {
        return albumRepository.save(album);
    }

    public Album findByName(String name) {
        return albumRepository.findByName(name).stream().findFirst().orElse(null);
    }

    public List<Album> findAll(UserSession userSession) {
        if (userSession.isLoggedIn()) {
            return albumRepository.findAll();
        } else {
            throw new SecurityException("You must be logged in to view all albums");
        }
    }

    public void releaseAlbum(ArtistUser artist, Album album) {
        artist.notifyFollowers(album);
    }

    public void delete(UserSession userSession, String idStr) throws NumberFormatException {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            Long id = Long.parseLong(idStr);
            Optional<Album> optional = albumRepository.findById(id);
            if (optional.isPresent()) {
                Album album = optional.get();

                // remove the links between the songs and album
                Iterator<Song> iterator = album.getSongs().iterator();
                while (iterator.hasNext()) {
                    Song song = iterator.next();
                    song.setAlbum(null);
                    songService.save(song);
                    iterator.remove();
                }

                albumRepository.deleteById(id);
            } else {
                throw new EntityNotFoundException("Album was not found");
            }
        }
        throw new SecurityException("Only admins can delete albums.");
    }
}
