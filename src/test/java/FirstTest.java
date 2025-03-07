
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FirstTest extends BaseTest {

    @Test
    public void testAmazonSearch() {
        ProductListPage plpPage = new HomePage().goToHomePage().search("Bambulab A1");
        plpPage.getHeader().clickOnHeaderLink("Ofertas");

        assertThat(plpPage.getTitle())
                .as("Checking title")
                .withFailMessage("\"Title expected to contain 'Ofertas' but got '%s'\"",plpPage.getTitle())
                .contains("ofertas1");

    }




}
