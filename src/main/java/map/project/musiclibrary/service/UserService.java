package map.project.musiclibrary.service;

import map.project.musiclibrary.data.repository.UserRepository;
import map.project.musiclibrary.data.repository.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findByName(String name) {
        return userRepository.findByName(name).stream().findFirst().orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
