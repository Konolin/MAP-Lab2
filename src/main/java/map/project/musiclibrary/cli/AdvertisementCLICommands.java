package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.repository.model.Advertisement;
import map.project.musiclibrary.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

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

    @ShellMethod
    public String addAdvertisement(@ShellOption(value = {"name"}, help = "Name of the advertisement") final String name,
                                   @ShellOption(value = {"length"}, help = "Length of the advertisement") final String length,
                                   @ShellOption(value = {"type"}, help = "The type of the advertisement") final String type) {
        Advertisement advertisement = new Advertisement();
        advertisement.setName(name);
        advertisement.setLength(Integer.parseInt(length));
//        advertisement.setAdvertisementType();
//        advertisement.setReleaseDate();
        // TODO - aici
//        advertisement.setPodcasts();
//        advertisement.setCompany();
        return advertisementService.save(advertisement).toString();
    }
}
