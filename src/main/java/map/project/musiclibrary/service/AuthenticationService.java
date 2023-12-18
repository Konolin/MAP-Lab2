package map.project.musiclibrary.service;

import map.project.musiclibrary.data.model.users.Admin;
import map.project.musiclibrary.data.model.users.LoginCredentials;
import map.project.musiclibrary.data.model.users.NormalUser;
import map.project.musiclibrary.data.model.users.User;
import map.project.musiclibrary.data.repository.LoginCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {
    private final LoginCredentialsRepository loginCredentialsRepository;

    @Autowired
    public AuthenticationService(LoginCredentialsRepository loginCredentialsRepository) {
        this.loginCredentialsRepository = loginCredentialsRepository;
    }

    public User login(String email, String password) {
        List<LoginCredentials> loginCredentialsList = loginCredentialsRepository.findByEmailAndPassword(email, password);

        if (!loginCredentialsList.isEmpty() && loginCredentialsList.getFirst().getUser().isNormalUser()) {
            return loginCredentialsList.getFirst().getUser();
        }
        if (isAdminCredentials(email, password)) {
            return Admin.getInstance();
        }
        return null;
    }

    private boolean isAdminCredentials(String email, String password) {
        return "admin".equals(email) && "admin".equals(password);
    }
}
