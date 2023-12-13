package map.project.musiclibrary.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import map.project.musiclibrary.data.model.misc.Notification;
import map.project.musiclibrary.data.model.users.ArtistUser;
import map.project.musiclibrary.data.model.users.LoginCredentials;
import map.project.musiclibrary.data.model.users.NormalUser;
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

    public NormalUser addNormalUser(String name, String email, String password, String isPremiumStr, String birthdateStr) throws ParseException {
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
        Date birthdate = dateFormat.parse(birthdateStr);
        user.setBirthdate(birthdate);

        return normalUserRepository.save(user);
    }

    public void deleteNormalUser(Long id){
        normalUserRepository.deleteById(id);
    }

    public String updateUser(Long id, Map<String, Object> updates) {
        Optional<NormalUser> optionalNormalUser = normalUserRepository.findById(id);

        if (optionalNormalUser.isPresent()) {
            NormalUser normalUser = optionalNormalUser.get();

            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String attribute = entry.getKey();

                if (attribute.equals("password")) {
                    String newPassword = promptForNewPassword();
                    if (!normalUser.getLoginCredentials().getPassword().equals(newPassword)) {
                        if (newPassword.isEmpty())
                            return "Error: Failed to confirm new password";
                        normalUser.getLoginCredentials().setPassword(newPassword);
                    } else {
                        return "Error: New password can't be the same as the old password.";
                    }
                }

                if (attribute.equals("isPremium")) {
                    if (!changeSubscriptionPlan(normalUser))
                        return "The subscription plan you chose matches your current plan.";
                }
            }

            normalUserRepository.save(normalUser);
            return "Changes saved!";
        } else {
            throw new EntityNotFoundException("User not found!");
        }
    }



    //helper method to change the password
    public String promptForNewPassword(){
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
        return normalUserRepository.findByName(name).stream().findFirst().orElse(null);
    }

    public List<NormalUser> findAll() {
        return normalUserRepository.findAll();
    }

    public NormalUser login(String email, String password) {
        List<LoginCredentials> loginCredentialsList = loginCredentialsRepository.findByEmailAndPassword(email, password);
        if (loginCredentialsList.isEmpty()) {
            return null;
        }
        return loginCredentialsList.get(0).getUser();
    }

    @Transactional
    public void followArtist(NormalUser user, Long artistId) {
        Optional<ArtistUser> artistUserOptional = artistUserService.findById(artistId);
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

    @Transactional
    public void unfollowArtist(NormalUser user, String artistIdStr) throws NumberFormatException {
        Long artistId = Long.parseLong(artistIdStr);

        Optional<ArtistUser> artistUserOptional = artistUserService.findById(artistId);
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

    public List<Notification> getNotifications(NormalUser normalUser) {
        List<Notification> notifications = normalUser.getNotifications();
        normalUser.setNotifications(new ArrayList<>());
        return notifications;
    }
}
