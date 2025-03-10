package pages;

import org.openqa.selenium.By;
import pages.setup.BasePage;

public class Footer extends BasePage {

    private static final By FOOTER_COPYRIGHT = By.cssSelector("div[data-test='footer-copy']");
    private static final By TWITTER_LINK = By.cssSelector("a[data-test='social-twitter']");
    private static final By FACEBOOK_LINK = By.cssSelector("a[data-test='social-facebook']");
    private static final By LINKEDIN_LINK = By.cssSelector("a[data-test='social-linkedin']");

    public Footer() {
        super();
    }

    public String getFooterText() {
        return getText(FOOTER_COPYRIGHT);
    }

    public void openTwitter() {
        click(TWITTER_LINK);
    }

    public void openFacebook() {
        click(FACEBOOK_LINK);
    }

    public void openLinkedIn() {
        click(LINKEDIN_LINK);
    }
}
