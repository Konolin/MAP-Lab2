package map.project.musiclibrary;

import map.project.musiclibrary.data.model.NormalUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@DataJpaTest
class NormalUserTest {

    private NormalUser normalUser = new NormalUser();

    @Test
    void testNormalUserEntity() {

        //test getters + setters
        normalUser.setId(1L);  //L in this case represents a cast to Long
        normalUser.setName("John Doe");
        //normalUser.setEmail("test@example.com");
        normalUser.setBirthdate(new Date());
        normalUser.setPremium(true);

        Long id = normalUser.getId();
        String name = normalUser.getName();
        //String email = normalUser.getEmail();
        Date birthdate = normalUser.getBirthdate();
        boolean isPremium = normalUser.isPremium();

        //assert that the retrieved values match the expected values
        assertEquals(1L, id);
        assertEquals("John Doe", name);
        //assertEquals("test@example.com", email);
        assertNotNull(birthdate);
        assertTrue(isPremium);
    }

    @Test
    void testNormalUserEntityException() {
        //test exception handling
        assertThrows(IllegalArgumentException.class, () -> normalUser.setBirthdate(null));
    }
}