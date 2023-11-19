package map.project.musiclibrary.data.model.users;

import map.project.musiclibrary.data.model.users.User;
import org.springframework.stereotype.Component;

@Component
public class UserSession {

    private User currentUser;

    public void login(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
