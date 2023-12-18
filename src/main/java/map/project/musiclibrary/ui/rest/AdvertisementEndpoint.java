package map.project.musiclibrary.ui.rest;

import map.project.musiclibrary.data.dto.AdvertisementDTO;
import map.project.musiclibrary.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/advertisement")
public class AdvertisementEndpoint {
    private final AdvertisementService advertisementService;

    @Autowired
    public AdvertisementEndpoint(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @GetMapping("/list")
    public String listAdvertisements() {
        try {
            return advertisementService.findAll().toString();
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/add")
    public String addAdvertisement(@RequestBody AdvertisementDTO request) {
        try {
            return advertisementService.addAdvertisement(request.getName(), request.getLength(), request.getType(), request.getReleaseDate()).toString();
        } catch (ParseException e) {
            return "Error: Invalid birthdate format. Please use yyyy-MM-dd.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/find")
    public String findAd(@RequestParam String name) {
        try {
            return advertisementService.findByName(name).toString();
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @DeleteMapping("/delete")
    public String deleteAd(@RequestParam String idStr) {
        try {
            advertisementService.delete(idStr);
            return "Advertisement with ID " + idStr + " has been deleted successfully!";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
