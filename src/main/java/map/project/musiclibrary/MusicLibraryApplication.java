package map.project.musiclibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MusicLibraryApplication {
    public static void main(String[] args) {
        SpringApplication.run(MusicLibraryApplication.class, args);
    }
}

// TODO - toString mai frumoase (cu \n; fara toate paranteztele etc)
// TODO - adminLogin si login sa fie o singura chestie ex: login --email admin --password admin -> te logheaza ca admin
// TODO - date format (sa nu mai fie si ora)
// TODO - userSession sa fie singleton si static
// TODO - exceptions mai frumoase (Locatie::metoda::mesaj)
// TODO - json format fix