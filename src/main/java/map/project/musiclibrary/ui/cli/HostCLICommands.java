package map.project.musiclibrary.ui.cli;

import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.users.Admin;
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
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            return hostUserService.findAll().toString();
        } else {
            return "Only admin can list all hosts";
        }
    }

    @ShellMethod(key = "addHost", value = "Add a host")
    public String addHost(@ShellOption(value = {"name"}, help = "Name of the host") final String name,
                          @ShellOption(value = {"birthdate"}, help = "Birthdate of the user (yyyy-MM-dd)") final String birthdateStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            try {
                return hostUserService.addHost(name, birthdateStr).toString();
            } catch (ParseException e) {
                return "Error: Invalid birthdate format. Please use yyyy-MM-dd.";
            }
        } else {
            return "Only admin can add a host";
        }
    }

    @ShellMethod(key = "deleteHost", value = "Delete a host (Deletes their podcasts as well!")
    public String deleteHost(@ShellOption(value = {"hostId"}, help = "ID of the host to be deleted") final String hostIdStr) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            try {
                Long hostId = Long.parseLong(hostIdStr);
                hostUserService.deleteHost(hostId);
                return "Host with ID " + hostId + " has been deleted successfully!";
            } catch (IllegalArgumentException e) {
                return "Invalid id format";
            } catch (EntityNotFoundException e) {
                return "Host was not found";
            }
        } else {
            return "Only admin can delete hosts.";
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
        } catch (RuntimeException e) {
            return "Error: Host with specified id doesn't exist";
        }
    }
}
