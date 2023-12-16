package map.project.musiclibrary.ui.rest;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.dto.NormalUserDTO;
import map.project.musiclibrary.data.model.misc.Notification;
import map.project.musiclibrary.data.model.users.NormalUser;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.NormalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/normalUser")
public class NormalUserEndpoint {
    private final NormalUserService normalUserService;
    private final UserSession userSession;

    @Autowired
    public NormalUserEndpoint(NormalUserService normalUserService, UserSession userSession) {
        this.normalUserService = normalUserService;
        this.userSession = userSession;
    }

    @GetMapping("/list")
    public String listUsers() {
        try {
            return normalUserService.findAll(userSession).toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/add")
    public String addUser(@RequestBody NormalUserDTO request) {
        try {
            return normalUserService.addNormalUser(userSession, request.getName(), request.getEmail(), request.getPassword(), request.getIsPremiumStr(), request.getBirthdateStr()).toString();
        } catch (EntityExistsException | IllegalArgumentException | SecurityException e) {
            return e.getMessage();
        }
    }

    @DeleteMapping("/delete")
    public String deleteUser(@RequestParam String idStr) {
        try {
            normalUserService.deleteNormalUser(userSession, idStr);
            return "User with ID " + idStr + " has been deleted successfully!";
        } catch (NumberFormatException e) {
            return "Error: Invalid id format";
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @PutMapping("/update")
    public String updateUser(@RequestParam boolean updatePassword, @RequestParam boolean updatePremium) {
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

    @GetMapping("/getByName")
    public String getByName(@RequestParam String name) {
        try {
            return normalUserService.findByName(userSession, name).toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @PutMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        NormalUser user = normalUserService.login(email, password);
        if (user != null) {
            userSession.login(user);
            return "Login successful. Welcome, " + userSession.getCurrentUser().getName();
        } else {
            return "Invalid credentials. Please try again.";
        }
    }

    @PutMapping("/logout")
    public String logout() {
        if (userSession.isLoggedIn()) {
            String goodbyeMessage = "Logout successful. Goodbye " + userSession.getCurrentUser().getName();
            userSession.logout();
            return goodbyeMessage;
        } else {
            return "No user is currently logged in.";
        }
    }

    @GetMapping("/currentUserInfo")
    public String currentUserInfo() {
        if (userSession.isLoggedIn()) {
            return userSession.getCurrentUser().toString();
        } else {
            return "No user is currently logged in.";
        }
    }

    @PutMapping("/followArtist")
    public String followArtist(@RequestParam String artistId) {
        try {
            normalUserService.followArtist(userSession, artistId);
            return "You are now following the artist with ID " + artistId;
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        } catch (SecurityException | EntityNotFoundException e) {
            return e.getMessage();
        }
    }

    @PutMapping("/unfollowArtist")
    public String unfollowArtist(@RequestParam String artistId) {
        try {
            normalUserService.unfollowArtist(userSession, artistId);
            return "You have unfollowed the artist with ID " + artistId;
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        } catch (EntityNotFoundException | SecurityException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/notifications")
    public String seeNewNotifications() {
        try {
            List<Notification> notifications = normalUserService.getNotifications(userSession);
            return (!notifications.isEmpty()) ? notifications.toString() : "No new notifications";
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }
}
