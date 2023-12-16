package map.project.musiclibrary.ui.rest;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/list")
    public String listLabels() {
        try {
            return labelService.findAll(userSession).toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/add")
    public String addLabel(@RequestParam String name) {
        try {
            return labelService.addLabel(userSession, name).toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @DeleteMapping("/delete")
    public String deleteLabel(@RequestParam String labelIdStr) {
        try {
            labelService.deleteLabel(userSession, labelIdStr);
            return "Label with ID " + labelIdStr + " has been deleted successfully!";
        } catch (NumberFormatException e) {
            return "Invalid id format";
        } catch (EntityNotFoundException | SecurityException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/find")
    public String findLabel(@RequestParam String name) {
        return labelService.findByName(name).toString();
    }

    @PostMapping("/addArtist")
    public String addArtistToLabel(@RequestParam String artistIdStr, @RequestParam String labelIdStr) {
        try {
            return labelService.addArtist(userSession, artistIdStr, labelIdStr).toString();
        } catch (IllegalArgumentException e) {
            return "Invalid id format";
        } catch (EntityNotFoundException | SecurityException e) {
            return e.getMessage();
        }
    }
}
