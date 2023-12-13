package map.project.musiclibrary.data.model.users;

import org.springframework.stereotype.Component;

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

    @Override
    public boolean isAdmin() {
        return true;
    }
}
