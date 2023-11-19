package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.model.users.Admin;
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
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            return advertisementService.findAll().toString();
        } else {
            return "Only admin can list all ads";
        }
    }

    @ShellMethod(key = "addAd", value = "Add an advertisement")
    public String addAdvertisement(@ShellOption(value = {"name"}, help = "Name of the advertisement") final String name,
                                   @ShellOption(value = {"length"}, help = "Length of the advertisement") final String length,
                                   @ShellOption(value = {"type"}, help = "The type of the advertisement") final String type,
                                   @ShellOption(value = {"releaseDate"}, help = "The release date of the ad (yyyy-MM-dd)") final String releaseDate) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            try {
                return advertisementService.addAdvertisement(name, length, type, releaseDate).toString();
            } catch (ParseException e) {
                return "Error: Invalid birthdate format. Please use yyyy-MM-dd.";
            }
        } else {
            return "Only admin can add ads";
        }
    }

    @ShellMethod(key = "findAd", value = "Find an ad by name")
    public String findAd(@ShellOption(value = {"name"}, help = "Name of the ad") final String name) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            return advertisementService.findByName(name).toString();
        } else {
            return "Only admin can search for ads";
        }
    }
}
