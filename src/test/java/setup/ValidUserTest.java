package setup;

import models.User;
import models.UserType;
import org.junit.jupiter.api.BeforeEach;
import pages.LandingPage;
import utils.TestDataManager;

public class ValidUserTest extends BaseTest {
    private User user;

    @BeforeEach
    public void login() {
        LandingPage home = new LandingPage();
        this.user = TestDataManager.getUser(UserType.VALID_USER);
        home.login(user);
    }

    public User getUser() {
        return user;
    }
}
