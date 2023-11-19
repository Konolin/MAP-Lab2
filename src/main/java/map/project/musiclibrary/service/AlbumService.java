package map.project.musiclibrary.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import map.project.musiclibrary.data.model.audios.Album;
import map.project.musiclibrary.data.model.users.ArtistUser;
import map.project.musiclibrary.data.repository.AlbumRepository;
import map.project.musiclibrary.service.builders.AlbumBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumService {
    @Getter
    private final AlbumRepository albumRepository;
    private final ArtistUserService artistUserService;
    private final SongService songService;


    @Autowired
    public AlbumService(AlbumRepository albumRepository, ArtistUserService artistUserService, SongService songService) {
        this.albumRepository = albumRepository;
        this.artistUserService = artistUserService;
        this.songService = songService;
    }

    public Album addAlbum(String name, String artistIdStr, String songIdsStr) {
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

    public Album findByName(String name) {
        return albumRepository.findByName(name).stream().findFirst().orElse(null);
    }

    public List<Album> findAll() {
        return albumRepository.findAll();
    }

    @Transactional // TODO dupa refactor verificat daca mai ii necesar tagul asta
    public void releaseAlbum(ArtistUser artist, Album album) {
        artist.notifyFollowers(album);
    }
}
