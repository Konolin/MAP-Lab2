package map.project.musiclibrary.data.model.users;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class UserSession {
    @Getter
    private static User currentUser;

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
