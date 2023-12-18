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
        if (UserSession.isLoggedIn()) {
            return podcastService.findAll().toString();
        } else {
            return "You must be logged in to see all podcasts";
        }
    }

    @PostMapping("/add")
    public String addPodcast(@RequestBody PodcastDTO request) {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isAdmin()) {
            try {
                return podcastService.addPodcast(request.getName(), request.getLengthStr(), request.getTopic(), request.getReleaseDateStr(), request.getHostIdStr()).toString();
            } catch (IllegalArgumentException e) {
                return e.getMessage();
            }
        } else {
            return "Only admins may add podcasts";
        }
    }

    @DeleteMapping("/delete")
    public String deletePodcast(@RequestParam String podcastIdStr) {
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

    @GetMapping("/find")
    public String findPodcast(@RequestParam String name) {
        return podcastService.findByName(name).toString();
    }

    @PostMapping("/addAd")
    public String addAd(@RequestParam String podcastIdStr, @RequestParam String adIdStr) {
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

    @PostMapping("/play")
    public String playPodcast(@RequestParam String podcastName) {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isNormalUser()) {
            return podcastService.playPodcast(podcastName);
        }
        return "You must log into a normal user account to play a podcast.";
    }

    @PostMapping("/playSpeed")
    public String playPodcastSpeed(@RequestParam String podcastName, @RequestParam String speed) {
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
