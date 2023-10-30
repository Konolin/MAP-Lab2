package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.repository.model.User;
import map.project.musiclibrary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ShellComponent
public class UserCLICommands {
    private final UserService userService;

    @Autowired
    public UserCLICommands(UserService userService) {
        this.userService = userService;
    }

    @ShellMethod(key = "list", value = "List all users")
    public String listUsers() {
        return userService.findAll().toString();
    }

    @ShellMethod(key = "add", value = "Add a user")
    public String addUser(@ShellOption(value = {"name"}, help = "Name of the user") final String name,
                          @ShellOption(value = {"email"}, help = "Email of the user") final String email,
                          @ShellOption(value = {"birthdate"}, help = "Birthdate of the user (yyyy-MM-dd)") final String birthdateString) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date birthdate = dateFormat.parse(birthdateString);
            user.setBirthdate(birthdate);
        } catch (ParseException e) {
            return "Error: Invalid birthdate format. Please use yyyy-MM-dd.";
        }
        return userService.save(user).toString();
    }

    @ShellMethod(key = "find", value = "Find user")
    public String findUser(@ShellOption(value = {"name"}, help = "Name of the user") final String name) {
        return userService.findByName(name).toString();
    }
}
