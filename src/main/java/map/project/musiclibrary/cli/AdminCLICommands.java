package map.project.musiclibrary.cli;

import map.project.musiclibrary.data.model.Admin;
import map.project.musiclibrary.data.model.UserSession;
import map.project.musiclibrary.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class AdminCLICommands {

    private final AdminService adminService;
    private final UserSession userSession;

    @Autowired
    public AdminCLICommands(AdminService adminService, UserSession userSession) {
        this.adminService = adminService;
        this.userSession = userSession;
    }

    @ShellMethod(key = "adminLogin", value = "Log in as an admin")
    public String adminLogin(@ShellOption(value = {"email"}, help = "Email of the admin") final String email,
                             @ShellOption(value = {"password"}, help = "Password of the admin") final String password) {
        Admin admin = adminService.login(email, password);

        if (admin != null) {
            userSession.login(admin);
            return "Admin login successful";
        } else {
            return "Invalid admin credentials. Please try again.";
        }
    }

    @ShellMethod(key = "adminLogout", value = "Logout as an admin")
    public String adminLogout() {
        userSession.logout();
        return "Admin logout successful.";
    }
}

