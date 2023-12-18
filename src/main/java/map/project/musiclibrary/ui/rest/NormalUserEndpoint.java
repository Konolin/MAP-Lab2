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

    @Autowired
    public NormalUserEndpoint(NormalUserService normalUserService) {
        this.normalUserService = normalUserService;
    }

    @GetMapping("/list")
    public String listUsers() {
        try {
            return normalUserService.findAll().toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/add")
    public String addUser(@RequestBody NormalUserDTO request) {
        try {
            return normalUserService.addNormalUser(request.getName(), request.getEmail(), request.getPassword(), request.getIsPremiumStr(), request.getBirthdateStr()).toString();
        } catch (EntityExistsException | IllegalArgumentException | SecurityException e) {
            return e.getMessage();
        }
    }

    @DeleteMapping("/delete")
    public String deleteUser(@RequestParam String idStr) {
        try {
            normalUserService.deleteNormalUser(idStr);
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
            Long id = UserSession.getCurrentUser().getId();
            Map<String, Object> updates = new HashMap<>();
            if (updatePassword) {
                updates.put("password", true);
            }
            if (updatePremium) {
                updates.put("isPremium", true);
            }
            return normalUserService.updateUser(id, updates);
        } catch (NumberFormatException e) {
            return "Error: Invalid user ID format. Please provide a valid number.";
        } catch (SecurityException | IllegalArgumentException | EntityNotFoundException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/getByName")
    public String getByName(@RequestParam String name) {
        try {
            return normalUserService.findByName(name).toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @PutMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        NormalUser user = normalUserService.login(email, password);
        if (user != null) {
            UserSession.login(user);
            return "Login successful. Welcome, " + UserSession.getCurrentUser().getName();
        } else {
            return "Invalid credentials. Please try again.";
        }
    }

    @PutMapping("/logout")
    public String logout() {
        if (UserSession.isLoggedIn()) {
            String goodbyeMessage = "Logout successful. Goodbye " + UserSession.getCurrentUser().getName();
            UserSession.logout();
            return goodbyeMessage;
        } else {
            return "No user is currently logged in.";
        }
    }

    @GetMapping("/currentUserInfo")
    public String currentUserInfo() {
        if (UserSession.isLoggedIn()) {
            return UserSession.getCurrentUser().toString();
        } else {
            return "No user is currently logged in.";
        }
    }

    @PutMapping("/followArtist")
    public String followArtist(@RequestParam String artistId) {
        try {
            normalUserService.followArtist(artistId);
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
            normalUserService.unfollowArtist(artistId);
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
            List<Notification> notifications = normalUserService.getNotifications();
            return (!notifications.isEmpty()) ? notifications.toString() : "No new notifications";
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }
}
