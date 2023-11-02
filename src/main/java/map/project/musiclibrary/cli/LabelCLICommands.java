package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.model.Label;
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
        return labelService.findAll().toString();
    }

    @ShellMethod(key = "addLabel", value = "Add a label")
    public String addLabel(@ShellOption(value = {"name"}, help = "Name of the label") final String name) {
        Label label = new Label();
        label.setName(name);
        return labelService.save(label).toString();
    }

    @ShellMethod(key = "findLabel", value = "Find a label by name")
    public String findLabel(@ShellOption(value = {"name"}, help = "Name of the label") final String name) {
        return labelService.findByName(name).toString();
    }
}
