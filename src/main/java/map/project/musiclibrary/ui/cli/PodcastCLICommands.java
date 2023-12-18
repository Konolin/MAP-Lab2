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
        if (UserSession.isLoggedIn()) {
            return podcastService.findAll().toString();
        } else {
            return "You must be logged in to see all podcasts";
        }
    }

    @ShellMethod(key = "addPodcast", value = "Add a podcast")
    public String addPodcast(@ShellOption(value = {"name"}, help = "Name of the podcast") final String name,
                             @ShellOption(value = {"length"}, help = "Length of the podcast(in seconds)") final String lengthStr,
                             @ShellOption(value = {"topic"}, help = "The topic of the podcast") final String topic,
                             @ShellOption(value = {"releaseDate"}, help = "The release date of the podcast") final String releaseDateStr,
                             @ShellOption(value = {"hostId"}, help = "The id of the host") final String hostIdStr) {
        //check if the currentUser is an admin, because only admins can add podcasts to the library
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isAdmin()) {
            try {
                return podcastService.addPodcast(name, lengthStr, topic, releaseDateStr, hostIdStr).toString();
            } catch (IllegalArgumentException e) {
                return e.getMessage();
            }
        } else {
            return "Only admins may add podcasts";
        }
    }

    @ShellMethod(key = "deletePodcast", value = "Delete a podcast by ID")
    public String deletePodcast(@ShellOption(value = {"podcastId"}, help = "ID of the podcast to be deleted") final String podcastIdStr) {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isAdmin()) {
            try {
                Long podcastId = Long.parseLong(podcastIdStr);
                podcastService.deletePodcast(podcastId);
                return "Podcast with ID " + podcastId + " has been deleted successfully!";
            } catch (IllegalArgumentException e) {
                return "Error: Invalid id format";
            } catch (EntityNotFoundException e) {
                return "Error: Podcast was not found";
            }
        } else {
            return "Only admin can delete a podcast";
        }
    }

    @ShellMethod(key = "findPodcast", value = "Find a podcast by name")
    public String findPodcast(@ShellOption(value = {"name"}, help = "Name of the podcast") final String name) {
        return podcastService.findByName(name).toString();
    }

    @ShellMethod(key = "addAdToPodcast", value = "Add an advertisement to a podcast")
    public String addAdToPodcast(@ShellOption(value = {"adId"}, help = "Id of the advertisement") final String adIdStr,
                                 @ShellOption(value = {"podcastId"}, help = "Id of the podcast") final String podcastIdStr) {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isAdmin()) {
            try {
                return podcastService.addAdToPodcast(adIdStr, podcastIdStr).toString();
            } catch (NumberFormatException e) {
                return "Error: Invalid integer format. Please provide a valid number.";
            } catch (RuntimeException e) {
                return "Advertisement or podcast with specified id doesn't exist";
            }
        } else {
            return "Only admin can add an ad to a podcast";
        }
    }

    @ShellMethod(key = "playPodcast", value = "Play a podcast by name")
    public String playPodcast(@ShellOption(value = {"name"}, help = "Name of the podcast") final String podcastName) {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isNormalUser()) {
            return podcastService.playPodcast(podcastName);
        }
        return "You must log into a normal user account to play a podcast.";
    }

    @ShellMethod(key = "playPodcastSpeed", value = "Play a podcast by name with a specified speed")
    public String playPodcastSpeed(@ShellOption(value = {"name"}, help = "Name of the podcast") final String podcastName,
                                   @ShellOption(value = {"speed"}, help = "Speed of the podcast") final String speed) {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isNormalUser() && UserSession.getCurrentUser().isPremiumUser()) {
            try {
                return podcastService.playPodcastSpeed(podcastName, speed);
            } catch (EntityNotFoundException e) {
                return e.getMessage();
            } catch (NumberFormatException e) {
                return "Error: Invalid integer format. Please provide a valid number.";
            }
        }
        return "You must log into a premium normal user account to play a podcast.";
    }
}
