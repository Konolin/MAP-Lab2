package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.repository.model.HostUser;
import map.project.musiclibrary.data.repository.model.Podcast;
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

    @Autowired
    public PodcastCLICommands(PodcastService podcastService, HostUserService hostUserService) {
        this.podcastService = podcastService;
        this.hostUserService = hostUserService;
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
            Long hostId = Long.parseLong(hostIdStr);
            Optional<HostUser> hostUserOptional = hostUserService.findById(hostId);
            if (hostUserOptional.isPresent()) {
                podcast.setHost(hostUserOptional.get());
                hostUserOptional.get().addPodcast(podcast);
            } else {
                return "Error: A host with that id does not exist";
            }
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        }

        return podcastService.save(podcast).toString();
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
}
