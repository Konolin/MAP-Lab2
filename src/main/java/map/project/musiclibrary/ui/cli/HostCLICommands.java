package map.project.musiclibrary.ui.cli;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.HostUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.text.ParseException;

@ShellComponent
public class HostCLICommands {
    private final HostUserService hostUserService;
    private final UserSession userSession;

    @Autowired
    public HostCLICommands(HostUserService hostUserService, UserSession userSession) {
        this.hostUserService = hostUserService;
        this.userSession = userSession;
    }

    @ShellMethod(key = "listHosts", value = "List all hosts")
    public String listHostUsers() {
        try {
            return hostUserService.findAll(userSession).toString();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "addHost", value = "Add a host")
    public String addHost(@ShellOption(value = {"name"}, help = "Name of the host") final String name,
                          @ShellOption(value = {"birthdate"}, help = "Birthdate of the user (yyyy-MM-dd)") final String birthdateStr) {
        try {
            return hostUserService.addHost(userSession, name, birthdateStr).toString();
        } catch (SecurityException | IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "deleteHost", value = "Delete a host (Deletes their podcasts as well!")
    public String deleteHost(@ShellOption(value = {"hostId"}, help = "ID of the host to be deleted") final String hostIdStr) {
        try {
            hostUserService.deleteHost(userSession, hostIdStr);
            return "Host with ID " + hostIdStr + " has been deleted successfully!";
        } catch (NumberFormatException e) {
            return "Invalid id format";
        } catch (EntityNotFoundException | SecurityException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "findHost", value = "Find a host by name")
    public String findHost(@ShellOption(value = {"name"}, help = "Name of the host") final String name) {
        return hostUserService.findByName(name).toString();
    }

    @ShellMethod(key = "listHostsPodcasts", value = "List all the podcasts of a specific host")
    public String listHostsPodcasts(@ShellOption(value = {"id"}, help = "Id of the host") final String idStr) {
        try {
            return hostUserService.listHostsPodcasts(idStr).toString();
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        } catch (EntityNotFoundException e) {
            return e.getMessage();
        }
    }
}
