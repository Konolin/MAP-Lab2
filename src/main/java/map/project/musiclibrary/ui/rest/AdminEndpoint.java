package map.project.musiclibrary.ui.rest;

import map.project.musiclibrary.data.dto.AdminDTO;
import map.project.musiclibrary.data.model.users.Admin;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.data.dto.LoginResponseDTO;
import map.project.musiclibrary.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminEndpoint {
    private final AdminService adminService;
    private final UserSession userSession;

    @Autowired
    public AdminEndpoint(AdminService adminService, UserSession userSession) {
        this.adminService = adminService;
        this.userSession = userSession;
    }

    @PostMapping("/login")
    public LoginResponseDTO adminLogin(@ModelAttribute AdminDTO request) {
        Admin admin = adminService.login(request.getEmail(), request.getPassword());

        LoginResponseDTO response = new LoginResponseDTO();

        if (admin != null) {
            userSession.login(admin);
            response.setStatus("success");
            response.setMessage("Admin login successful");
        } else {
            response.setStatus("error");
            response.setMessage("Invalid admin credentials. Please try again.");
        }

        return response;
    }

    @PostMapping("/logout")
    public LoginResponseDTO adminLogout() {
        LoginResponseDTO response = new LoginResponseDTO();

        userSession.logout();
        response.setStatus("success");
        response.setMessage("Admin logout successful.");

        return response;
    }
}
