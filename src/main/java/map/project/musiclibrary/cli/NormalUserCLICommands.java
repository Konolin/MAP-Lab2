package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.repository.model.NormalUser;
import map.project.musiclibrary.service.NormalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ShellComponent
public class NormalUserCLICommands {
    private final NormalUserService normalUserService;

    @Autowired
    public NormalUserCLICommands(NormalUserService normalUserService) {
        this.normalUserService = normalUserService;
    }

    @ShellMethod(key = "listUsers", value = "List all users")
    public String listUsers() {
        return normalUserService.findAll().toString();
    }

    @ShellMethod(key = "addUser", value = "Add a user")
    public String addUser(@ShellOption(value = {"name"}, help = "Name of the user") final String name,
                          @ShellOption(value = {"email"}, help = "Email of the user") final String email,
                          @ShellOption(value = {"birthdate"}, help = "Birthdate of the user (yyyy-MM-dd)") final String birthdateString) {
        NormalUser user = new NormalUser();
        user.setName(name);
        user.setEmail(email);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date birthdate = dateFormat.parse(birthdateString);
            user.setBirthdate(birthdate);
        } catch (ParseException e) {
            return "Error: Invalid birthdate format. Please use yyyy-MM-dd.";
        }
        return normalUserService.save(user).toString();
    }

    @ShellMethod(key = "findUser", value = "Find user")
    public String findUser(@ShellOption(value = {"name"}, help = "Name of the user") final String name) {
        return normalUserService.findByName(name).toString();
    }
}
