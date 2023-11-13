package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.model.Admin;
import map.project.musiclibrary.data.model.Label;
import map.project.musiclibrary.data.model.UserSession;
import map.project.musiclibrary.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.ArrayList;

@ShellComponent
public class LabelCLICommands {
    private final LabelService labelService;
    private final UserSession userSession;

    @Autowired
    public LabelCLICommands(LabelService labelService, UserSession userSession) {
        this.labelService = labelService;
        this.userSession = userSession;
    }

    @ShellMethod(key = "listLabels", value = "List all labels")
    public String listLabels() {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            return labelService.findAll().toString();
        } else {
            throw new RuntimeException("Only admin can list all labels");
        }
    }

    @ShellMethod(key = "addLabel", value = "Add a label")
    public String addLabel(@ShellOption(value = {"name"}, help = "Name of the label") final String name) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            Label label = new Label();
            label.setName(name);
            label.setArtists(new ArrayList<>());
            return labelService.save(label).toString();
        } else {
            throw new RuntimeException("Only admin can add a label");
        }
    }

    @ShellMethod(key = "findLabel", value = "Find a label by name")
    public String findLabel(@ShellOption(value = {"name"}, help = "Name of the label") final String name) {
        return labelService.findByName(name).toString();
    }

    @ShellMethod(key = "addArtistToLabel", value = "Add an artist to a label")
    public String addArtistToLabel(@ShellOption(value = {"artistId"}, help = "Id of the artist") final String artistIdStr,
                                   @ShellOption(value = {"labelId"}, help = "Id of the label") final String labelIdStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            try {
                Long artistId = Long.parseLong(artistIdStr);
                Long labelId = Long.parseLong(labelIdStr);
                return labelService.addArtist(artistId, labelId).toString();
            } catch (NumberFormatException e) {
                return "Error: Invalid integer format. Please provide a valid number.";
            }
        } else {
            throw new RuntimeException("Only admin can add artists to a label");
        }
    }
}
