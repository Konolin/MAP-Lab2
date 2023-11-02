package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.repository.model.Podcast;
import map.project.musiclibrary.service.PodcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ShellComponent
public class PodcastCLICommands {
    private final PodcastService podcastService;

    @Autowired
    public PodcastCLICommands(PodcastService podcastService) {
        this.podcastService = podcastService;
    }

    @ShellMethod(key = "listPodcasts", value = "List all podcasts")
    public String listPodcasts() {
        return podcastService.findAll().toString();
    }

    @ShellMethod(key = "addPodcast", value = "Add a podcast")
    public String addPodcast(@ShellOption(value = {"name"}, help = "Name of the podcast") final String name,
                             @ShellOption(value = {"length"}, help = "Length of the podcast(in seconds)") final String lengthStr,
                             @ShellOption(value = {"topic"}, help = "The topic of the podcast") final String topic,
                             @ShellOption(value = {"releaseDate"}, help = "The release date of the podcast") final String releaseDateStr) {
        Podcast podcast = new Podcast();

        podcast.setName(name);
        podcast.setTopic(topic);
        try {
            int length = Integer.parseInt(lengthStr);
            podcast.setLength(length);
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date releaseDate = dateFormat.parse(releaseDateStr);
            podcast.setReleaseDate(releaseDate);
        } catch (ParseException e) {
            return "Error: Invalid birthdate format. Please use yyyy-MM-dd.";
        }
        // TODO - add chestiile astea in podcastCLI
//        podcast.setHost();
//        podcast.setAdvertisements();

        return podcastService.save(podcast).toString();
    }

    @ShellMethod(key = "findPodcast", value = "Find a podcast by name")
    public String findPodcast(@ShellOption(value = {"name"}, help = "Name of the podcast") final String name) {
        return podcastService.findByName(name).toString();
    }
}
