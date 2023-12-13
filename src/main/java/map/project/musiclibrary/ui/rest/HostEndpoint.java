package map.project.musiclibrary.ui.rest;

import map.project.musiclibrary.data.dto.HostDTO;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.HostUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/host")
public class HostEndpoint {
    private final HostUserService hostUserService;
    private final UserSession userSession;

    @Autowired
    public HostEndpoint(HostUserService hostUserService, UserSession userSession) {
        this.hostUserService = hostUserService;
        this.userSession = userSession;
    }

    @PostMapping("/list")
    public String listHostUsers() {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            return hostUserService.findAll().toString();
        } else {
            return "You must log in to list all hosts.";
        }
    }

    @PostMapping("/add")
    public String addHost(@RequestBody HostDTO request) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            try {
                return hostUserService.addHost(request.getName(), request.getBirthdate()).toString();
            } catch (Exception e) {
                return "Error: Invalid birthdate format. Please use yyyy-MM-dd.";
            }
        } else {
            return "You must log in as an admin to add a host.";
        }
    }

    @PostMapping("/delete")
    public String deleteHost(@RequestParam String idStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            try {
                Long hostId = Long.parseLong(idStr);
                hostUserService.deleteHost(hostId);
                return "Host with ID " + hostId + " has been deleted successfully!";
            } catch (IllegalArgumentException e) {
                return "Invalid id format";
            } catch (Exception e) {
                return "Host was not found";
            }
        } else {
            return "You must log in as an admin to delete a host.";
        }
    }

    @PostMapping("/find")
    public String findHost(@RequestParam String name) {
        return hostUserService.findByName(name).toString();
    }

    @PostMapping("listPodcasts")
    public String listPodcasts(@RequestParam String idStr) {
        try {
            return hostUserService.listHostsPodcasts(idStr).toString();
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        } catch (RuntimeException e) {
            return "Error: Host with specified id doesn't exist";
        }
    }
}
