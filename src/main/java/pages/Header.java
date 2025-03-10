package pages;

import org.openqa.selenium.By;
import pages.setup.BasePage;

public class Header extends BasePage {

    private static final By CART_BADGE = By.cssSelector("span[data-test='shopping-cart-badge']");
    private static final By CART_LINK = By.cssSelector("a[data-test='shopping-cart-link']");
    private static final By MENU_BUTTON = By.cssSelector("button[id='react-burger-menu-btn']");
    private static final By LOGOUT_LINK = By.cssSelector("a[data-test='logout-sidebar-link']");

    public Header() {
        super();
    }

    public int getCartBadgeCount() {
        String countText = getText(CART_BADGE);
        return countText.isEmpty() ? 0 : Integer.parseInt(countText);
    }

    public CartPage goToCart() {

        click(CART_LINK);
        return new CartPage();
    }

    public void logout() {
        click(MENU_BUTTON);
        click(LOGOUT_LINK);
    }
}