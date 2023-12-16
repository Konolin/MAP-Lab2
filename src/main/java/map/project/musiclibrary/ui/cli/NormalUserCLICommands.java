package map.project.musiclibrary.ui.cli;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.misc.Notification;
import map.project.musiclibrary.data.model.users.NormalUser;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.NormalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ShellComponent
public class NormalUserCLICommands {
    private final NormalUserService normalUserService;
    private final UserSession userSession;

    @Autowired
    public NormalUserCLICommands(NormalUserService normalUserService, UserSession userSession) {
        this.normalUserService = normalUserService;
        this.userSession = userSession;
    }

    @ShellMethod(key = "listUsers", value = "List all users")
    public String listUsers() {
        try {
            return normalUserService.findAll(userSession).toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "addUser", value = "Add a user")
    public String addUser(@ShellOption(value = {"name"}, help = "Name of the user") final String name,
                          @ShellOption(value = {"email"}, help = "Email of the user") final String email,
                          @ShellOption(value = {"password"}, help = "Password of the user") final String password,
                          @ShellOption(value = {"birthdate"}, help = "Birthdate of the user (yyyy-MM-dd)") final String birthdateStr,
                          @ShellOption(value = {"isPremium"}, help = "Is the user premium (boolean)") final String isPremiumStr) {
        try {
            return normalUserService.addNormalUser(userSession, name, email, password, isPremiumStr, birthdateStr).toString();
        } catch (EntityExistsException | IllegalArgumentException | SecurityException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "deleteUser", value = "Delete a user")
    public String deleteUser(@ShellOption(value = {"userId"}, help = "Id of the user to be deleted") final String userIdStr) {
        try {
            normalUserService.deleteNormalUser(userSession, userIdStr);
            return "User with ID " + userIdStr + " has been deleted.";
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "updateUser", value = "Update user attributes")
    public String updateUser(@ShellOption(value = {"password"}, help = "Update user password") final boolean updatePassword,
                             @ShellOption(value = {"isPremium"}, help = "Update subscription plan") final boolean updatePremium) {
        try {
            Long id = userSession.getCurrentUser().getId();
            Map<String, Object> updates = new HashMap<>();
            if (updatePassword) {
                updates.put("password", true);
            }
            if (updatePremium) {
                updates.put("isPremium", true);
            }
            return normalUserService.updateUser(userSession, id, updates);
        } catch (NumberFormatException e) {
            return "Error: Invalid user ID format. Please provide a valid number.";
        } catch (SecurityException | IllegalArgumentException | EntityNotFoundException e) {
            return e.getMessage();
        }
    }


    @ShellMethod(key = "findUser", value = "Find a user by name")
    public String findUser(@ShellOption(value = {"name"}, help = "Name of the user") final String name) {
        try {
            return normalUserService.findByName(userSession, name).toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "login", value = "Log in as a NormalUser")
    public String login(@ShellOption(value = {"email"}, help = "Email of the user") final String email,
                        @ShellOption(value = {"password"}, help = "Password of the user") final String password) {
        NormalUser user = normalUserService.login(email, password);
        if (user != null) {
            userSession.login(user);
            return "Login successful. Welcome, " + userSession.getCurrentUser().getName();
        } else {
            return "Invalid credentials. Please try again.";
        }
    }

    @ShellMethod(key = "logout", value = "Log out the current user")
    public String logout() {
        if (userSession.isLoggedIn()) {
            String goodbyeMessage = "Logout successful. Goodbye " + userSession.getCurrentUser().getName();
            userSession.logout();
            return goodbyeMessage;
        } else {
            return "No user is currently logged in.";
        }
    }


    @ShellMethod(key = "currentUser", value = "Get the current user that is logged in")
    public String getCurrentUser() {
        if (userSession.isLoggedIn()) {
            return this.userSession.getCurrentUser().toString();
        } else {
            return "No user is currently logged in.";
        }
    }

    @ShellMethod(key = "followArtist", value = "Follow an artist")
    public String followArtist(@ShellOption(value = {"artistId"}, help = "ID of the artist") final String artistId) {
        try {
            normalUserService.followArtist(userSession, artistId);
            return "You are now following the artist with ID " + artistId;
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        } catch (SecurityException | EntityNotFoundException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "unfollowArtist", value = "Unfollow an artist")
    public String unfollowArtist(@ShellOption(value = {"artistId"}, help = "ID of the artist") final String artistId) {
        try {
            normalUserService.unfollowArtist(userSession, artistId);
            return "You have unfollowed the artist with ID " + artistId;
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        } catch (EntityNotFoundException | SecurityException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "seeNewNotifications", value = "See new notifications")
    public String seeNewNotifications() {
        try {
            List<Notification> notifications = normalUserService.getNotifications(userSession);
            return (!notifications.isEmpty()) ? notifications.toString() : "No new notifications";
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }
}