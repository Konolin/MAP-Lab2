package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.model.Advertisement;
import map.project.musiclibrary.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ShellComponent
public class AdvertisementCLICommands {
    private final AdvertisementService advertisementService;

    @Autowired
    public AdvertisementCLICommands(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @ShellMethod(key = "listAds", value = "List all advertisements")
    public String listAdvertisements() {
        return advertisementService.findAll().toString();
    }

    @ShellMethod(key = "addAd", value = "Add an advertisement")
    public String addAdvertisement(@ShellOption(value = {"name"}, help = "Name of the advertisement") final String name,
                                   @ShellOption(value = {"length"}, help = "Length of the advertisement") final String length,
                                   @ShellOption(value = {"type"}, help = "The type of the advertisement") final String type,
                                   @ShellOption(value = {"releaseDate"}, help = "The release date of the ad (yyyy-MM-dd)") final String releaseDateStr) {
        Advertisement advertisement = new Advertisement();

        advertisement.setName(name);
        advertisement.setLength(Integer.parseInt(length));
        advertisement.setAdvertisementType(type);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date releaseDate = dateFormat.parse(releaseDateStr);
            advertisement.setReleaseDate(releaseDate);
        } catch (ParseException e) {
            return "Error: Invalid birthdate format. Please use yyyy-MM-dd.";
        }

        return advertisementService.save(advertisement).toString();
    }

    @ShellMethod(key = "findAd", value = "Find an ad by name")
    public String findAd(@ShellOption(value = {"name"}, help = "Name of the ad") final String name) {
        return advertisementService.findByName(name).toString();
    }
}
