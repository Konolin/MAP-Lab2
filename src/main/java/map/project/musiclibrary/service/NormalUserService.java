package map.project.musiclibrary.service;

import map.project.musiclibrary.data.model.*;
import map.project.musiclibrary.data.repository.LoginCredentialsRepository;
import map.project.musiclibrary.data.repository.NormalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class NormalUserService {
    private final NormalUserRepository normalUserRepository;
    private final LoginCredentialsRepository loginCredentialsRepository;

    @Autowired
    public NormalUserService(NormalUserRepository normalUserRepository, LoginCredentialsRepository loginCredentialsRepository) {
        this.normalUserRepository = normalUserRepository;
        this.loginCredentialsRepository = loginCredentialsRepository;
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

    public void playAudio(Playable playable, boolean isPremium){
        Random randomChance = new Random();
        if(!isPremium && randomChance.nextInt(100) <= 33){  //33 represents the chance of an ad pop-up (33%)
            Advertisement ad = new Advertisement();
            ad.play();

            try{
                Thread.sleep(ad.getLength());
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        System.out.println("Now playing " + ((Audio) playable).getName());
        playable.play();
    }
}
