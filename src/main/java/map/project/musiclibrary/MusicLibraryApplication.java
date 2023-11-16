package map.project.musiclibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MusicLibraryApplication {
    // TODO - get artist name from song, remove circular dependency album-artist-song
    public static void main(String[] args) {
        SpringApplication.run(MusicLibraryApplication.class, args);
    }

}

//TODO - implement o clasa Notification ca sa pot stoca mesajele ce trebuie sa le trimit userilor odata ce se logheaza cand se lanseaza un nou album
