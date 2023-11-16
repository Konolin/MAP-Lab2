//package map.project.musiclibrary;
//
//import map.project.musiclibrary.data.repository.NormalUserRepository;
//import map.project.musiclibrary.data.model.NormalUser;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//public class NormalUserRepoTest {
//
//    @Autowired
//    private NormalUserRepository normalUserRepository;
//
//    @Test
//    void testFindNormalUserByName() {
//        NormalUser user = new NormalUser();
//        user.setName("John Doe");
//        normalUserRepository.save(user);
//
//        List<NormalUser> foundUsers = normalUserRepository.findByName("John Doe");
//
//        assertEquals(1, foundUsers.size());
//        assertEquals("John Doe", foundUsers.get(0).getName());
//    }
//}
