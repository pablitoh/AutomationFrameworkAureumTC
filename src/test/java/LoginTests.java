import models.User;
import models.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.LandingPage;
import pages.ProductListPage;
import setup.BaseTest;
import utils.TestDataManager;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Login Tests")
public class LoginTests extends BaseTest {

    @Test
    @Tag("Login")
    @DisplayName("TC-0001 | Verify login")
    public void verifyLogin() {
        LandingPage home = new LandingPage();
        User user = TestDataManager.getUser(UserType.VALID_USER);
        home.login(user);
        ProductListPage productListPage = new ProductListPage();
        assertThat(productListPage.areAllImportantElementsDisplayed())
                .as("Verify user can login and reach the Products page")
                .isTrue();
    }

    @Test
    @Tag("Login")
    @DisplayName("TC-0002 | Verify invalid login")
    public void verifyInvalidLogin() {
        LandingPage home = new LandingPage();
        User user = TestDataManager.getUser(UserType.INVALID_USER);
        home.fillLoginForm(user.getUsername(), user.getPassword());
        assertThat(home.getErrorMessage())
                .as("Verify error message for invalid login")
                .containsIgnoringCase("Epic sadface: Username and password do not match any user in this service");
    }

    @Test
    @Tag("Login")
    @DisplayName("TC-0003 | Verify locked-out user")
    public void verifyLockedOutUser() {
        LandingPage home = new LandingPage();
        User user = TestDataManager.getUser(UserType.LOCKED_OUT_USER);
        home.fillLoginForm(user.getUsername(), user.getPassword());
        assertThat(home.getErrorMessage())
                .as("Verify error message for locked-out user")
                .containsIgnoringCase("Epic sadface: Sorry, this user has been locked out.");
    }
}
