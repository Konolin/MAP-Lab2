package map.project.musiclibrary;

import map.project.musiclibrary.data.model.Admin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AdminSingletonTest {

    @Test
    void testSingletonUniqueness(){
        Admin admin1 = Admin.getInstance();
        Admin admin2 = Admin.getInstance();

        assertSame(admin1, admin2);
    }

    @Test
    void testAdminToString(){
        Admin admin = Admin.getInstance();

        assertEquals("Admin", admin.toString());
    }
}
