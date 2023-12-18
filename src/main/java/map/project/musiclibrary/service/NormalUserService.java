package map.project.musiclibrary.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.misc.Notification;
import map.project.musiclibrary.data.model.users.*;
import map.project.musiclibrary.data.repository.LoginCredentialsRepository;
import map.project.musiclibrary.data.repository.NormalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class NormalUserService {
    private final NormalUserRepository normalUserRepository;
    private final LoginCredentialsRepository loginCredentialsRepository;
    private final ArtistUserService artistUserService;

    @Autowired
    public NormalUserService(NormalUserRepository normalUserRepository, LoginCredentialsRepository loginCredentialsRepository, ArtistUserService artistUserService) {
        this.normalUserRepository = normalUserRepository;
        this.loginCredentialsRepository = loginCredentialsRepository;
        this.artistUserService = artistUserService;
    }

    public NormalUser addNormalUser(String name, String email, String password, String isPremiumStr, String birthdateStr) {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isAdmin()) {
            NormalUser user = new NormalUser();
            user.setName(name);

            LoginCredentials loginCredentials = new LoginCredentials();

            if (Objects.equals(email, "admin")) {
                throw new IllegalArgumentException("Email can not be set to admin");
            } else if (loginCredentialsRepository.findByEmail(email).isEmpty()) {
                loginCredentials.setEmail(email);
                loginCredentials.setPassword(password);
            } else {
                throw new EntityExistsException("Email already in use!");
            }

            user.setLoginCredentials(loginCredentials);
            loginCredentials.setUser(user);

            boolean isPremium = Boolean.parseBoolean(isPremiumStr);
            user.setPremium(isPremium);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date birthdate;
            try {
                birthdate = dateFormat.parse(birthdateStr);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid birthdate format. Please use yyyy-MM-dd.");
            }
            user.setBirthdate(birthdate);

            return normalUserRepository.save(user);
        }
        throw new SecurityException("Only admin can add users.");
    }

    public void deleteNormalUser(String idStr) throws NumberFormatException {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isAdmin()) {
            Long id = Long.parseLong(idStr);
            normalUserRepository.deleteById(id);
        }
        throw new SecurityException("Only admin can delete users.");
    }

    public String updateUser(Long id, Map<String, Object> updates) {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isNormalUser()) {
            Optional<NormalUser> optionalNormalUser = normalUserRepository.findById(id);
            if (optionalNormalUser.isPresent()) {
                NormalUser normalUser = optionalNormalUser.get();
                for (Map.Entry<String, Object> entry : updates.entrySet()) {
                    String attribute = entry.getKey();
                    if (attribute.equals("password")) {
                        String newPassword = promptForNewPassword();
                        if (!normalUser.getLoginCredentials().getPassword().equals(newPassword)) {
                            if (newPassword.isEmpty()) {
                                throw new IllegalArgumentException("Error: Password can't be empty.");
                            }
                            normalUser.getLoginCredentials().setPassword(newPassword);
                        } else {
                            throw new IllegalArgumentException("The password you chose matches your current password.");
                        }
                    }
                    if (attribute.equals("isPremium") && !changeSubscriptionPlan(normalUser)) {
                        throw new IllegalArgumentException("Error: The subscription plan you chose matches your current plan.");
                    }
                }
                normalUserRepository.save(normalUser);
                return "Changes saved!";
            } else {
                throw new EntityNotFoundException("User not found!");
            }
        }
        throw new SecurityException("Only normal users can update their profile.");
    }


    //helper method to change the password
    public String promptForNewPassword() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the new password: ");
        String newPassword = scanner.nextLine();
        System.out.println("Confirm new password: ");
        if (scanner.nextLine().equals(newPassword))
            return newPassword;
        return "";
    }

    //helper method to change subscription plan
    public boolean changeSubscriptionPlan(NormalUser normalUser) {
        System.out.println(normalUser.isPremium()
                ? "Would you like to unsubscribe from the Premium Plan? (yes/no): "
                : "Would you like to subscribe to the Premium Plan? (yes/no): ");

        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine().toLowerCase();

        if (userInput.equals("yes")) {
            normalUser.setPremium(!normalUser.isPremium());
            return true;
        }

        return false;
    }

    public NormalUser save(NormalUser user) {
        return normalUserRepository.save(user);
    }

    public NormalUser findByName(String name) {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser() instanceof Admin) {
            return normalUserRepository.findByName(name).stream().findFirst().orElse(null);
        }
        throw new SecurityException("Only admin can view all users.");
    }

    public List<NormalUser> findAll() {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isAdmin()) {
            return normalUserRepository.findAll();
        }
        throw new SecurityException("Only admin can view all users.");
    }

    public NormalUser login(String email, String password) {
        List<LoginCredentials> loginCredentialsList = loginCredentialsRepository.findByEmailAndPassword(email, password);
        if (loginCredentialsList.isEmpty()) {
            return null;
        }
        return loginCredentialsList.getFirst().getUser();
    }

    @Transactional
    public void followArtist(String artistIdStr) throws NumberFormatException {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isNormalUser()) {
            Long artistId = Long.parseLong(artistIdStr);
            Optional<ArtistUser> artistUserOptional = artistUserService.findById(artistId);

            NormalUser user = (NormalUser) UserSession.getCurrentUser();
            Optional<NormalUser> userOptional = normalUserRepository.findById(user.getId());

            if (artistUserOptional.isPresent() && userOptional.isPresent()) {
                ArtistUser artist = artistUserOptional.get();
                NormalUser currentUser = userOptional.get();
                artist.addFollower(currentUser);
                artistUserService.save(artist);
            } else {
                throw new EntityNotFoundException("Artist or user not found.");
            }
        }
        throw new SecurityException("Only normal users can follow artists.");
    }

    @Transactional
    public void unfollowArtist(String artistIdStr) throws NumberFormatException {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isNormalUser()) {
            Long artistId = Long.parseLong(artistIdStr);
            Optional<ArtistUser> artistUserOptional = artistUserService.findById(artistId);

            NormalUser user = (NormalUser) UserSession.getCurrentUser();
            Optional<NormalUser> userOptional = normalUserRepository.findById(user.getId());

            if (artistUserOptional.isPresent() && userOptional.isPresent()) {
                ArtistUser artist = artistUserOptional.get();
                NormalUser currentUser = userOptional.get();
                artist.removeFollower(currentUser);
                artistUserService.save(artist);
            } else {
                throw new EntityNotFoundException("Artist or user not found.");
            }
        }
        throw new SecurityException("Only normal users can unfollow artists.");
    }

    public List<Notification> getNotifications() {
        if (UserSession.isLoggedIn() && UserSession.getCurrentUser().isNormalUser()) {
            NormalUser normalUser = (NormalUser) UserSession.getCurrentUser();
            List<Notification> notifications = normalUser.getNotifications();
            normalUser.setNotifications(new ArrayList<>());
            return notifications;
        }
        throw new SecurityException("Only normal users can see their notifications.");
    }
}
