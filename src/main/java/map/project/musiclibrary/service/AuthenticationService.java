package map.project.musiclibrary.service;

import map.project.musiclibrary.data.model.LoginCredentials;
import map.project.musiclibrary.data.model.NormalUser;
import map.project.musiclibrary.data.model.UserSession;
import map.project.musiclibrary.data.repository.LoginCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
 public class AuthenticationService {

        @Autowired
        private UserSession userSession;

        @Autowired
        private LoginCredentialsRepository loginCredentialsRepository;

        public NormalUser login(String email, String password) {
            List<LoginCredentials> loginCredentialsList = loginCredentialsRepository.findByEmailAndPassword(email, password);
            if (loginCredentialsList.isEmpty()) {
                return null;
            }
            return loginCredentialsList.get(0).getUser();
        }
 }
