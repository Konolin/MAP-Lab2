package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.model.*;
import map.project.musiclibrary.service.HostUserService;
import map.project.musiclibrary.service.PodcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

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

    @ShellMethod(key = "listPodcasts", value = "List all podcasts")
    public String listPodcasts() {
        return podcastService.findAll().toString();
    }

    @ShellMethod(key = "addPodcast", value = "Add a podcast")
    public String addPodcast(@ShellOption(value = {"name"}, help = "Name of the podcast") final String name,
                             @ShellOption(value = {"length"}, help = "Length of the podcast(in seconds)") final String lengthStr,
                             @ShellOption(value = {"topic"}, help = "The topic of the podcast") final String topic,
                             @ShellOption(value = {"releaseDate"}, help = "The release date of the podcast") final String releaseDateStr,
                             @ShellOption(value = {"hostId"}, help = "The id of the host") final String hostIdStr) {

        //check if the currentUser is an admin, because only admins can add podcasts to the library
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
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

            try {
                // search host by id
                Long hostId = Long.parseLong(hostIdStr);
                Optional<HostUser> hostUserOptional = hostUserService.findById(hostId);
                if (hostUserOptional.isPresent()) {
                    // add host to podcast and add podcast to hosts list
                    podcast.setHost(hostUserOptional.get());
                    hostUserOptional.get().addPodcast(podcast);
                } else {
                    return "Error: A host with that id does not exist";
                }
            } catch (NumberFormatException e) {
                return "Error: Invalid integer format. Please provide a valid number.";
            }

            return podcastService.save(podcast).toString();
        } else {
            throw new RuntimeException("Only admins may add podcasts");
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
            Long adId = Long.parseLong(adIdStr);
            Long podcastId = Long.parseLong(podcastIdStr);
            return podcastService.addAd(adId, podcastId).toString();
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        }
    }

    //TODO - de structurat mai bine metodele (de ex sa fie clar ce am nevoie in service/CLI ca sa mentin encapsularea)
    //TODO - cand apare un Ad, nu se poate identifica ce nume are pentru ca e null for now
    @ShellMethod(key = "playPodcast", value = "Play a podcast by ID")
    public void playPodcast(@ShellOption(value = {"podcastId"}, help = "ID of the podcast") final String podcastIdstr) {
        if (!userSession.isLoggedIn()) {
            throw new RuntimeException("You must log in to play a podcast.");
        }
        Long podcastId = Long.parseLong(podcastIdstr);
        if (userSession.getCurrentUser() instanceof NormalUser) {
            podcastService.playPodcast(podcastId, (NormalUser) userSession.getCurrentUser());
        } else {
            throw new RuntimeException("Only normal users can play podcasts");
        }
    }
}
