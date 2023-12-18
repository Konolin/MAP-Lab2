package map.project.musiclibrary.ui.cli;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class LabelCLICommands {
    private final LabelService labelService;

    @Autowired
    public LabelCLICommands(LabelService labelService) {
        this.labelService = labelService;
    }

    @ShellMethod(key = "listLabels", value = "List all labels")
    public String listLabels() {
        try {
            return labelService.findAll().toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "addLabel", value = "Add a label")
    public String addLabel(@ShellOption(value = {"name"}, help = "Name of the label") final String name) {
        try {
            return labelService.addLabel(name).toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "deleteLabel", value = "Delete a label by ID")
    public String deleteLabel(@ShellOption(value = {"labelId"}, help = "ID of the label to be removed") final String labelIdStr) {
        try {
            labelService.deleteLabel(labelIdStr);
            return "Label with ID " + labelIdStr + " has been deleted successfully!";
        } catch (NumberFormatException e) {
            return "Invalid id format";
        } catch (EntityNotFoundException | SecurityException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "findLabel", value = "Find a label by name")
    public String findLabel(@ShellOption(value = {"name"}, help = "Name of the label") final String name) {
        return labelService.findByName(name).toString();
    }

    @ShellMethod(key = "addArtistToLabel", value = "Add an artist to a label")
    public String addArtistToLabel(@ShellOption(value = {"artistId"}, help = "Id of the artist") final String artistIdStr,
                                   @ShellOption(value = {"labelId"}, help = "Id of the label") final String labelIdStr) {
        try {
            return labelService.addArtist(artistIdStr, labelIdStr).toString();
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        } catch (EntityNotFoundException | SecurityException e) {
            return e.getMessage();
        }
    }
}
