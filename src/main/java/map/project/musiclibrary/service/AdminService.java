package map.project.musiclibrary.service;

import map.project.musiclibrary.data.model.Admin;
import map.project.musiclibrary.data.repository.LoginCredentialsRepository;
import org.springframework.stereotype.Component;

@Component
public class AdminService {
    private final LoginCredentialsRepository loginCredentialsRepository;

    public AdminService(LoginCredentialsRepository loginCredentialsRepository) {
        this.loginCredentialsRepository = loginCredentialsRepository;
    }

    public Admin login(String email, String password) {
        if ("admin".equals(email) && "admin".equals(password)) {
            return Admin.getInstance();
        }
        return null;
    }
}
