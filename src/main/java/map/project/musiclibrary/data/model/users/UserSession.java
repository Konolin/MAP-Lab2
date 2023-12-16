package map.project.musiclibrary.data.model.users;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class UserSession {
    private User currentUser;

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
