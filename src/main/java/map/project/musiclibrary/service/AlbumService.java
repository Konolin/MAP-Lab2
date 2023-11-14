package map.project.musiclibrary.service;

import map.project.musiclibrary.data.model.Album;
import map.project.musiclibrary.data.model.ArtistUser;
import map.project.musiclibrary.data.model.NormalUser;
import map.project.musiclibrary.data.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void releaseAlbum(ArtistUser artist, Album album) {
        String notificationContent = "New album released: " + album.getName();
        artist.notifyFollowers(album);

        List<NormalUser> followers = artist.getFollowers();
        for (NormalUser follower : followers) {
            follower.update(notificationContent);
        }
    }
}
