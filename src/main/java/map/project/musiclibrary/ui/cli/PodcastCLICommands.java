package map.project.musiclibrary.ui.cli;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.PodcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class PodcastCLICommands {
    private final PodcastService podcastService;

    @Autowired
    public PodcastCLICommands(PodcastService podcastService) {
        this.podcastService = podcastService;
    }

    @ShellMethod(key = "listPodcasts", value = "List all podcasts")
    public String listPodcasts() {
        try {
            return podcastService.findAll().toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "addPodcast", value = "Add a podcast")
    public String addPodcast(@ShellOption(value = {"name"}, help = "Name of the podcast") final String name,
                             @ShellOption(value = {"length"}, help = "Length of the podcast(in seconds)") final String lengthStr,
                             @ShellOption(value = {"topic"}, help = "The topic of the podcast") final String topic,
                             @ShellOption(value = {"releaseDate"}, help = "The release date of the podcast") final String releaseDateStr,
                             @ShellOption(value = {"hostId"}, help = "The id of the host") final String hostIdStr) {
        try {
            return podcastService.addPodcast(name, lengthStr, topic, releaseDateStr, hostIdStr).toString();
        } catch (IllegalArgumentException | SecurityException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "deletePodcast", value = "Delete a podcast by ID")
    public String deletePodcast(@ShellOption(value = {"podcastId"}, help = "ID of the podcast to be deleted") final String podcastId) {
        try {
            podcastService.deletePodcast(podcastId);
            return "Podcast with ID " + podcastId + " has been deleted successfully!";
        } catch (IllegalArgumentException e) {
            return "Error: Invalid id format";
        } catch (EntityNotFoundException e) {
            return "Error: Podcast was not found";
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "findPodcast", value = "Find a podcast by name")
    public String findPodcast(@ShellOption(value = {"name"}, help = "Name of the podcast") final String name) {
        return podcastService.findByName(name).toString();
    }

    @ShellMethod(key = "addAdToPodcast", value = "Add an advertisement to a podcast")
    public String addAdToPodcast(@ShellOption(value = {"adId"}, help = "Id of the advertisement") final String adIdStr,
                                 @ShellOption(value = {"podcastId"}, help = "Id of the podcast") final String podcastIdStr) {
        try {
            return podcastService.addAdToPodcast(adIdStr, podcastIdStr).toString();
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        } catch (SecurityException | EntityNotFoundException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "playPodcast", value = "Play a podcast by name")
    public String playPodcast(@ShellOption(value = {"name"}, help = "Name of the podcast") final String podcastName) {
        try {
            return podcastService.playPodcast(podcastName);
        } catch (SecurityException | EntityNotFoundException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "playPodcastSpeed", value = "Play a podcast by name with a specified speed")
    public String playPodcastSpeed(@ShellOption(value = {"name"}, help = "Name of the podcast") final String podcastName,
                                   @ShellOption(value = {"speed"}, help = "Speed of the podcast") final String speed) {
        try {
            return podcastService.playPodcastSpeed(podcastName, speed);
        } catch (EntityNotFoundException | SecurityException e) {
            return e.getMessage();
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        }
    }
}
