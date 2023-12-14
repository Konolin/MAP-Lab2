package map.project.musiclibrary;

import map.project.musiclibrary.data.model.users.NormalUser;
import map.project.musiclibrary.data.repository.NormalUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NormalUserServiceTest {
    private final List<NormalUser> inMemoryRepository;
    private final NormalUserRepository normalUserRepository;

    @Autowired
    public NormalUserServiceTest(NormalUserRepository normalUserRepository) {
        this.inMemoryRepository = new ArrayList<>();
        this.normalUserRepository = normalUserRepository;
    }

    @Test
    void testSaveNormalUser() {
        NormalUser userToSave = new NormalUser();
        userToSave.setName("John Doe");
        userToSave.setBirthdate(new Date());
        userToSave.setPremium(true);

        NormalUser savedUser = normalUserRepository.save(userToSave);

        assertNotNull(savedUser.getId());
        assertEquals("John Doe", savedUser.getName());
    }

    @Test
    void testFindByName() {
        NormalUser user1 = new NormalUser();
        user1.setName("John Doe");
        normalUserRepository.save(user1);

        NormalUser user2 = new NormalUser();
        user2.setName("Jane Doe");
        normalUserRepository.save(user2);

        List<NormalUser> foundUsers = normalUserRepository.findByName("John Doe");

        assertFalse(foundUsers.isEmpty());
        assertEquals("John Doe", foundUsers.getFirst().getName());
    }


    @Test
    void testFindAll() {
        int expected = normalUserRepository.findAll().size();

        NormalUser user1 = new NormalUser();
        user1.setName("John Doe");
        inMemoryRepository.add(user1);

        NormalUser user2 = new NormalUser();
        user2.setName("Jane Doe");
        inMemoryRepository.add(user2);

        normalUserRepository.saveAll(inMemoryRepository);

        List<NormalUser> allUsers = normalUserRepository.findAll();

        assertFalse(allUsers.isEmpty());
        assertEquals(expected + 2, allUsers.size());
    }
}

