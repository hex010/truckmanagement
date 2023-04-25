package truck.truckmanagement.Service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import truck.truckmanagement.Enum.Role_enum;
import truck.truckmanagement.Model.Forum;
import truck.truckmanagement.Model.User;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.*;

public class UserIntegrationTest {
    private static EntityManagerFactory entityManagerFactory;
    private static UserService userService;

    private User userFromDB;

    @Before
    public void setup() {
        entityManagerFactory = Persistence.createEntityManagerFactory("TruckManagement");
        userService = new UserService(entityManagerFactory);
    }
    @After
    public void cleanup() {
        if(userFromDB != null) {
            userService.removeUser(userFromDB);
            userFromDB = null;
        }
        entityManagerFactory.close();
    }

    @Test
    public void testGetExistingUser() {
        User user = new User();
        user.setEmail("my-email");
        user.setPassword("my-password");
        user.setLogin("my-login");
        user.setFirstname("my-firstname");
        user.setLastname("my-lastname");
        user.setRole(Role_enum.VADYBININKAS);

        userService.createUser(user);

        userFromDB = userService.getUserByLoginData("my-login", "my-password");

        assertNotNull(userFromDB);
        assertEquals(user.getRole(), userFromDB.getRole());
        assertEquals(user.getLastname(), userFromDB.getLastname());
        assertEquals(user.getEmail(), userFromDB.getEmail());
    }

    @Test
    public void testGetNotExistingUser() {
        userFromDB = userService.getUserByLoginData("asdgadsg", "rthrthr");

        assertNull(userFromDB);
    }
}
