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
// TODO - mutare check admin/normal user in service
// TODO - decorator song cu lyrics pentru premium user