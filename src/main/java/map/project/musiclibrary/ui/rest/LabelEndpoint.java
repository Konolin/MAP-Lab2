package map.project.musiclibrary.ui.rest;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/labels")
public class LabelEndpoint {
    private final LabelService labelService;
    private final UserSession userSession;

    @Autowired
    public LabelEndpoint(LabelService labelService, UserSession userSession) {
        this.labelService = labelService;
        this.userSession = userSession;
    }

    @PostMapping("/list")
    public String listLabels() {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            return labelService.findAll().toString();
        } else {
            return "You must be logged in to see all labels";
        }
    }

    @PostMapping("/add")
    public String addLabel(@RequestParam String name) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            return labelService.addLabel(name).toString();
        } else {
            return "Only admin can add a label";
        }
    }

    @PostMapping("/delete")
    public String deleteLabel(@RequestParam String labelIdStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            try {
                Long labelId = Long.parseLong(labelIdStr);
                labelService.deleteLabel(labelId);
                return "Label with ID " + labelId + " has been deleted successfully!";
            } catch (IllegalArgumentException e) {
                return "Invalid id format";
            } catch (EntityNotFoundException e) {
                return "Label was not found";
            }
        } else {
            return "Only admin can delete labels.";
        }
    }

    @PostMapping("/find")
    public String findLabel(@RequestParam String name) {
        return labelService.findByName(name).toString();
    }

    @PostMapping("/addArtist")
    public String addArtistToLabel(@RequestParam String artistIdStr, @RequestParam String labelIdStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser().isAdmin()) {
            try {
                return labelService.addArtist(artistIdStr, labelIdStr).toString();
            } catch (IllegalArgumentException e) {
                return "Invalid id format";
            } catch (EntityNotFoundException e) {
                return "Artist or label was not found";
            }
        } else {
            return "Only admin can add artists to labels.";
        }
    }
}
