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
        try {
            return advertisementService.findAll(userSession).toString();
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/add")
    public String addAdvertisement(@RequestBody AdvertisementDTO request) {
        try {
            return advertisementService.addAdvertisement(userSession, request.getName(), request.getLength(), request.getType(), request.getReleaseDate()).toString();
        } catch (ParseException e) {
            return "Error: Invalid birthdate format. Please use yyyy-MM-dd.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/find")
    public String findAd(@RequestParam String name) {
        try {
            return advertisementService.findByName(userSession, name).toString();
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/delete")
    public String deleteAd(@RequestParam String idStr) {
        try {
            advertisementService.delete(userSession, idStr);
            return "Advertisement with ID " + idStr + " has been deleted successfully!";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
