import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParallelTest extends BaseTest{

    @Test
    public void testAmazonSearch2() {
        ProductListPage plpPage = new HomePage().goToHomePage().search("DJI Mavic 2");
        plpPage.getHeader().clickOnHeaderLink("Ofertas");
        assertThat(plpPage.getTitle())
                .as("Checking title")
                .withFailMessage("\"Title expected to contain 'Ofertas' but got '%s'\"",plpPage.getTitle())
                .contains("ofertas");
    }
    @Test
    public void testAmazonSearch3() {
        ProductListPage plpPage = new HomePage().goToHomePage().search("DJI Mavic 2");
        plpPage.getHeader().clickOnHeaderLink("Ofertas");
        assertThat(plpPage.getTitle())
                .as("Checking title")
                .withFailMessage("\"Title expected to contain 'Ofertas' but got '%s'\"",plpPage.getTitle())
                .containsIgnoringCase("ofertas");
    }
}
