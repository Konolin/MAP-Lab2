package map.project.musiclibrary.ui.cli;

import map.project.musiclibrary.data.model.users.Admin;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class AdminCLICommands {

    private final AdminService adminService;

    @Autowired
    public AdminCLICommands(AdminService adminService) {
        this.adminService = adminService;
    }

    @ShellMethod(key = "adminLogin", value = "Log in as an admin")
    public String adminLogin(@ShellOption(value = {"email"}, help = "Email of the admin") final String email,
                             @ShellOption(value = {"password"}, help = "Password of the admin") final String password) {
        Admin admin = adminService.login(email, password);

        if (admin != null) {
            UserSession.login(admin);
            return "Admin login successful";
        } else {
            return "Invalid admin credentials. Please try again.";
        }
    }

    @ShellMethod(key = "adminLogout", value = "Logout as an admin")
    public String adminLogout() {
        UserSession.logout();
        return "Admin logout successful.";
    }
}

