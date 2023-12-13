package map.project.musiclibrary.ui.rest;

import jakarta.persistence.EntityExistsException;
import map.project.musiclibrary.data.dto.NormalUserDTO;
import map.project.musiclibrary.data.model.users.Admin;
import map.project.musiclibrary.data.model.users.NormalUser;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.NormalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
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

    @PostMapping("/list")
    public String listUsers() {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            return normalUserService.findAll().toString();
        } else {
            return "Only admin can list all users.";
        }
    }

    @PostMapping("/add")
    public String addUser(@RequestBody NormalUserDTO request) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            try {
                return normalUserService.addNormalUser(request.getName(), request.getEmail(), request.getPassword(), request.getIsPremiumStr(), request.getBirthdateStr()).toString();
            } catch (ParseException e) {
                return "Error: Invalid birthdate format. Please use yyyy-MM-dd.";
            } catch (EntityExistsException e) {
                return "Error: Email already in use.";
            } catch (IllegalArgumentException e) {
                return "Error: Email can not be set to admin.";
            }
        } else {
            return "Only admin can add users.";
        }
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam String idStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            try {
                Long id = Long.parseLong(idStr);
                normalUserService.deleteNormalUser(id);
                return "User with ID " + id + " has been deleted successfully!";
            } catch (IllegalArgumentException e) {
                return "Error: Invalid id format";
            }
        } else {
            return "Only admin can delete users.";
        }
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam boolean updatePassword, @RequestParam boolean updatePremium) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof NormalUser) {
            try {
                Long id = userSession.getCurrentUser().getId();
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
            }
        } else {
            return "Only normal users can modify their password/premium status";
        }
    }
}
