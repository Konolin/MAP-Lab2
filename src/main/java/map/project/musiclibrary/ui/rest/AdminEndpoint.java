package map.project.musiclibrary.ui.rest;

import map.project.musiclibrary.data.dto.AdminDTO;
import map.project.musiclibrary.data.dto.LoginResponseDTO;
import map.project.musiclibrary.data.model.users.Admin;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminEndpoint {
    private final AdminService adminService;

    @Autowired
    public AdminEndpoint(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public LoginResponseDTO adminLogin(@ModelAttribute AdminDTO request) {
        Admin admin = adminService.login(request.getEmail(), request.getPassword());

        LoginResponseDTO response = new LoginResponseDTO();

        if (admin != null) {
            UserSession.login(admin);
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

        UserSession.logout();
        response.setStatus("success");
        response.setMessage("Admin logout successful.");

        return response;
    }
}
