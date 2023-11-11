package map.project.musiclibrary.data.model;

import org.springframework.stereotype.Component;

@Component
public class UserSession {

    private NormalUser currentUser;

    public void login(NormalUser user) {
        this.currentUser = user;
    }

    public NormalUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(NormalUser currentUser) {
        this.currentUser = currentUser;
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
