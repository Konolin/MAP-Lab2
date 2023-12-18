package map.project.musiclibrary.ui.rest;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.dto.PodcastDTO;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.PodcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/podcasts")
public class PodcastEndpoint {
    private final PodcastService podcastService;

    @Autowired
    public PodcastEndpoint(PodcastService podcastService) {
        this.podcastService = podcastService;
    }

    @GetMapping("/list")
    public String listPodcasts() {
        try {
            return podcastService.findAll().toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/add")
    public String addPodcast(@RequestBody PodcastDTO request) {
        try {
            return podcastService.addPodcast(request.getName(), request.getLengthStr(), request.getTopic(), request.getReleaseDateStr(), request.getHostIdStr()).toString();
        } catch (IllegalArgumentException | SecurityException e) {
            return e.getMessage();
        }
    }

    @DeleteMapping("/delete")
    public String deletePodcast(@RequestParam String podcastId) {
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

    @GetMapping("/find")
    public String findPodcast(@RequestParam String name) {
        return podcastService.findByName(name).toString();
    }

    @PostMapping("/addAd")
    public String addAd(@RequestParam String podcastIdStr, @RequestParam String adIdStr) {
        try {
            return podcastService.addAdToPodcast(adIdStr, podcastIdStr).toString();
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        } catch (EntityNotFoundException | SecurityException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/play")
    public String playPodcast(@RequestParam String podcastName) {
        try {
            return podcastService.playPodcast(podcastName);
        } catch (SecurityException | EntityNotFoundException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/playSpeed")
    public String playPodcastSpeed(@RequestParam String podcastName, @RequestParam String speed) {
        try {
            return podcastService.playPodcastSpeed(podcastName, speed);
        } catch (EntityNotFoundException | SecurityException e) {
            return e.getMessage();
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        }
    }
}
