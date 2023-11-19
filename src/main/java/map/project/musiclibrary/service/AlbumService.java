package map.project.musiclibrary.service;

import map.project.musiclibrary.data.model.audios.Album;
import map.project.musiclibrary.data.model.users.ArtistUser;
import map.project.musiclibrary.data.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlbumService {
    final AlbumRepository albumRepository;

    @Autowired
    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public Album save(Album album) {
        return albumRepository.save(album);
    }

    public Album findByName(String name) {
        return albumRepository.findByName(name).stream().findFirst().orElse(null);
    }

    public List<Album> findAll() {
        return albumRepository.findAll();
    }

    @Transactional
    public void releaseAlbum(ArtistUser artist, Album album) {
        artist.notifyFollowers(album);
    }
}
