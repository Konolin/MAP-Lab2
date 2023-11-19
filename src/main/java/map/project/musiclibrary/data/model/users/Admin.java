package map.project.musiclibrary.data.model.users;

import org.springframework.stereotype.Component;

//create an admin instance and make sure is unique by implementing it using Singleton Design Pattern
@Component
public class Admin extends User {
    private static Admin instance;

    private Admin() {
    }

    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }

    @Override
    public String toString() {
        return "Admin";
    }
}
