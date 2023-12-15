package map.project.musiclibrary.ui.cli;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.text.ParseException;

@ShellComponent
public class AdvertisementCLICommands {
    private final AdvertisementService advertisementService;
    private final UserSession userSession;

    @Autowired
    public AdvertisementCLICommands(AdvertisementService advertisementService, UserSession userSession) {
        this.advertisementService = advertisementService;
        this.userSession = userSession;
    }

    @ShellMethod(key = "listAds", value = "List all advertisements")
    public String listAdvertisements() {
        try {
            return advertisementService.findAll(userSession).toString();
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "addAd", value = "Add an advertisement")
    public String addAdvertisement(@ShellOption(value = {"name"}, help = "Name of the advertisement") final String name,
                                   @ShellOption(value = {"length"}, help = "Length of the advertisement") final String length,
                                   @ShellOption(value = {"type"}, help = "The type of the advertisement") final String type,
                                   @ShellOption(value = {"releaseDate"}, help = "The release date of the ad (yyyy-MM-dd)") final String releaseDate) {
        try {
            return advertisementService.addAdvertisement(userSession, name, length, type, releaseDate).toString();
        } catch (ParseException e) {
            return "Error: Invalid birthdate format. Please use yyyy-MM-dd.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "findAd", value = "Find an ad by name")
    public String findAd(@ShellOption(value = {"name"}, help = "Name of the ad") final String name) {
        try {
            return advertisementService.findByName(userSession, name).toString();
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "deleteAd", value = "Delete an ad by id")
    public String deleteAd(@ShellOption(value = {"id"}, help = "Id of the ad") final String idStr) {
        try {
            advertisementService.delete(userSession, idStr);
            return "Ad successfully deleted.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
