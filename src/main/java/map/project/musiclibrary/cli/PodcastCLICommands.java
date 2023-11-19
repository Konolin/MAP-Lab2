package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.model.users.Admin;
import map.project.musiclibrary.data.model.users.NormalUser;
import map.project.musiclibrary.data.model.audios.Podcast;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.HostUserService;
import map.project.musiclibrary.service.builders.PodcastBuilder;
import map.project.musiclibrary.service.PodcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class PodcastCLICommands {
    private final PodcastService podcastService;
    private final HostUserService hostUserService;
    private final UserSession userSession;

    @Autowired
    public PodcastCLICommands(PodcastService podcastService, HostUserService hostUserService, UserSession userSession) {
        this.podcastService = podcastService;
        this.hostUserService = hostUserService;
        this.userSession = userSession;
    }

    // TODO - useri sa vada podcasturile
    @ShellMethod(key = "listPodcasts", value = "List all podcasts")
    public String listPodcasts() {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            return podcastService.findAll().toString();
        } else {
            throw new RuntimeException("Only admin can list all podcasts");
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
                Podcast podcast = new PodcastBuilder()
                        .setName(name)
                        .setLength(lengthStr)
                        .setTopic(topic)
                        .setReleaseDate(releaseDateStr)
                        .setHostId(hostIdStr)
                        .build(hostUserService);

                return podcastService.save(podcast).toString();
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
                Long adId = Long.parseLong(adIdStr);
                Long podcastId = Long.parseLong(podcastIdStr);
                return podcastService.addAd(adId, podcastId).toString();
            } catch (NumberFormatException e) {
                return "Error: Invalid integer format. Please provide a valid number.";
            }
        } else {
            throw new RuntimeException("Only admin can add an ad to a podcast");
        }
    }

    // TODO - play podcast by name not id
    @ShellMethod(key = "playPodcast", value = "Play a podcast by ID")
    public String playPodcast(@ShellOption(value = {"podcastId"}, help = "ID of the podcast") final String podcastIdStr) {
        if (!userSession.isLoggedIn()) {
            return "You must log in to play a podcast.";
        }
        if (userSession.getCurrentUser() instanceof NormalUser) {
            return podcastService.playPodcast(podcastIdStr);
        }
        return "Only normal users can play podcasts";
    }
}
