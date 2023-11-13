package map.project.musiclibrary.service;

import map.project.musiclibrary.data.model.Admin;
import map.project.musiclibrary.data.model.LoginCredentials;
import map.project.musiclibrary.data.model.NormalUser;
import map.project.musiclibrary.data.model.User;
import map.project.musiclibrary.data.repository.LoginCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    @Autowired
    private LoginCredentialsRepository loginCredentialsRepository;

    public User login(String email, String password) {
        List<LoginCredentials> loginCredentialsList = loginCredentialsRepository.findByEmailAndPassword(email, password);

        if (!loginCredentialsList.isEmpty() && loginCredentialsList.get(0).getUser() instanceof NormalUser) {
            return loginCredentialsList.get(0).getUser();
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
