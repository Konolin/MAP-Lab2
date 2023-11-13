package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.model.Admin;
import map.project.musiclibrary.data.model.HostUser;
import map.project.musiclibrary.data.model.UserSession;
import map.project.musiclibrary.service.HostUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
            throw new RuntimeException("Only admin can list all hosts");
        }
    }

    @ShellMethod(key = "addHost", value = "Add a host")
    public String addHost(@ShellOption(value = {"name"}, help = "Name of the host") final String name,
                          @ShellOption(value = {"birthdate"}, help = "Birthdate of the user (yyyy-MM-dd)") final String birthdateString) {
        if (userSession.isLoggedIn() && userSession.getCurrentUser() instanceof Admin) {
            HostUser host = new HostUser();

            host.setName(name);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date birthdate = dateFormat.parse(birthdateString);
                host.setBirthdate(birthdate);
            } catch (ParseException e) {
                return "Error: Invalid birthdate format. Please use yyyy-MM-dd.";
            }

            return hostUserService.save(host).toString();
        } else {
            throw new RuntimeException("Only admin can add a host");
        }
    }

    @ShellMethod(key = "findHost", value = "Find a host by name")
    public String findHost(@ShellOption(value = {"name"}, help = "Name of the host") final String name) {
        return hostUserService.findByName(name).toString();
    }

    @ShellMethod(key = "listHostsPodcasts", value = "List all the podcasts of a specific host")
    public String listHostsPodcasts(@ShellOption(value = {"id"}, help = "Id of the host") final String idStr) {
        try {
            Long id = Long.parseLong(idStr);
            return hostUserService.listHostsPodcasts(id).toString();
        } catch (NumberFormatException e) {
            return "Error: Invalid integer format. Please provide a valid number.";
        } catch (RuntimeException e) {
            return "Error: Host with specified id doesn't exist";
        }
    }
}
