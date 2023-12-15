package map.project.musiclibrary;

import map.project.musiclibrary.data.dto.AdminDTO;
import map.project.musiclibrary.data.dto.LoginResponseDTO;
import map.project.musiclibrary.data.model.users.Admin;
import map.project.musiclibrary.data.model.users.UserSession;
import map.project.musiclibrary.service.AdminService;
import map.project.musiclibrary.ui.rest.AdminEndpoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdminEndpointTest {

    @MockBean
    private AdminService adminService;
    @MockBean
    private UserSession userSession;
    private AdminEndpoint adminEndpoint;

    @BeforeEach
    void setUp() {
        adminEndpoint = new AdminEndpoint(adminService, userSession);
    }

    @Test
    public void testAdminLoginSuccess() {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setEmail("admin");
        adminDTO.setPassword("admin");

        Admin admin = Admin.getInstance();

        when(adminService.login(adminDTO.getEmail(), adminDTO.getPassword())).thenReturn(admin);

        LoginResponseDTO response = adminEndpoint.adminLogin(adminDTO);

        assertEquals("success", response.getStatus());
        assertEquals("Admin login successful", response.getMessage());
    }

    @Test
    public void testAdminLoginFailure() {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setEmail("admin");
        adminDTO.setPassword("admin");

        when(adminService.login(adminDTO.getEmail(), adminDTO.getPassword())).thenReturn(null);

        LoginResponseDTO response = adminEndpoint.adminLogin(adminDTO);

        assertEquals("error", response.getStatus());
        assertEquals("Invalid admin credentials. Please try again.", response.getMessage());
    }
}
