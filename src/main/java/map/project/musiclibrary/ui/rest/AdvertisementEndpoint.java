package map.project.musiclibrary.ui.rest;

import map.project.musiclibrary.data.dto.AdvertisementDTO;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/advertisement")
public class AdvertisementEndpoint {
    private final AdvertisementService advertisementService;
    private final UserSession userSession;

    @Autowired
    public AdvertisementEndpoint(AdvertisementService advertisementService, UserSession userSession) {
        this.advertisementService = advertisementService;
        this.userSession = userSession;
    }

    @PostMapping("/list")
    public String listAdvertisements() {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            return advertisementService.findAll().toString();
        } else {
            return "Only admin can list all ads";
        }
    }

    @PostMapping("/add")
    public String addAdvertisement(@RequestBody AdvertisementDTO request) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            try {
                return advertisementService.addAdvertisement(request.getName(), request.getLength(), request.getType(), request.getReleaseDate()).toString();
            } catch (ParseException e) {
                return "Error: Invalid birthdate format. Please use yyyy-MM-dd.";
            }
        } else {
            return "Only admin can add ads";
        }
    }

    @PostMapping("/find")
    public String findAd(@RequestParam String name) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            return advertisementService.findByName(name).toString();
        } else {
            return "Only admin can search for ads";
        }
    }

    @PostMapping("/delete")
    public String deleteAd(@RequestParam String idStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            try {
                advertisementService.delete(idStr);
                return "Advertisement with ID " + idStr + " has been deleted successfully!";
            } catch (IllegalArgumentException e) {
                return "Invalid id format";
            }
        } else {
            return "Only admin can delete ads.";
        }
    }
}
