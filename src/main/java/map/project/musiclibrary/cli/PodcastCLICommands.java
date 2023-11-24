package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.model.users.Admin;
import map.project.musiclibrary.data.model.users.NormalUser;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.PodcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class PodcastCLICommands {
    private final PodcastService podcastService;
    private final UserSession userSession;

    @Autowired
    public PodcastCLICommands(PodcastService podcastService, UserSession userSession) {
        this.podcastService = podcastService;
        this.userSession = userSession;
    }

    @ShellMethod(key = "listPodcasts", value = "List all podcasts")
    public String listPodcasts() {
        if (userSession.isLoggedIn()) {
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
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            try {
                return podcastService.adPodcast(name, lengthStr, topic, releaseDateStr, hostIdStr).toString();
            } catch (IllegalArgumentException e) {
                return e.getMessage();
            }
        } else {
            return "Only admins may add podcasts";
        }
    }

    @ShellMethod(key = "findPodcast", value = "Find a podcast by name")
    public String findPodcast(@ShellOption(value = {"name"}, help = "Name of the podcast") final String name) {
        return podcastService.findByName(name).toString();
    }

    @ShellMethod(key = "addAdToPodcast", value = "Add an advertisement to a podcast")
    public String addAdToPodcast(@ShellOption(value = {"adId"}, help = "Id of the advertisement") final String adIdStr,
                                 @ShellOption(value = {"podcastId"}, help = "Id of the podcast") final String podcastIdStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
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
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof NormalUser) {
            return podcastService.playPodcast(podcastName);
        }
        return "You must log into a normal user account to play a podcast.";
    }
}
