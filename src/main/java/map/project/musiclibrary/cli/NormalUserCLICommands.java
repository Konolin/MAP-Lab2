package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.model.LoginCredentials;
import map.project.musiclibrary.data.model.NormalUser;
import map.project.musiclibrary.data.model.User;
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
                          @ShellOption(value = {"password"}, help = "Password of the user") final String password,
                          @ShellOption(value = {"birthdate"}, help = "Birthdate of the user (yyyy-MM-dd)") final String birthdateString,
                          @ShellOption(value = {"isPremium"}, help = "Is the user premium (boolean)") final String isPremiumStr) {
        NormalUser user = new NormalUser();
        user.setName(name);

        LoginCredentials loginCredentials = new LoginCredentials();
        loginCredentials.setEmail(email);
        loginCredentials.setPassword(password);

        user.setLoginCredentials(loginCredentials);
        loginCredentials.setUser(user);

        try {
            boolean isPremium = Boolean.parseBoolean(isPremiumStr);
            user.setPremium(isPremium);
        } catch (NumberFormatException e) {
            return "Error: Invalid boolean string";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date birthdate = dateFormat.parse(birthdateString);
            user.setBirthdate(birthdate);
        } catch (ParseException e) {
            return "Error: Invalid birthdate format. Please use yyyy-MM-dd.";
        }
        return normalUserService.save(user).toString();
    }

    @ShellMethod(key = "findUser", value = "Find a user by name")
    public String findUser(@ShellOption(value = {"name"}, help = "Name of the user") final String name) {
        return normalUserService.findByName(name).toString();
    }
}
