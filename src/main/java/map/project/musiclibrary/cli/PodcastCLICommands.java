package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.model.Podcast;
import map.project.musiclibrary.service.HostUserService;
import map.project.musiclibrary.service.PodcastBuilder;
import map.project.musiclibrary.service.PodcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

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
