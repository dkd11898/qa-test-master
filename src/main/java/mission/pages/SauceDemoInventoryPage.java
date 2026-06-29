package mission.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class SauceDemoInventoryPage extends BasePage {

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartLink;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    public SauceDemoInventoryPage(WebDriver driver) {
        super(driver);
    }

    public void addItemsToCart(List<String> itemNames) {
        for (String itemName : itemNames) {
            click(waitUntilPresent(By.id("add-to-cart-" + slugify(itemName))));
        }
        waitUntil(driver -> getCartItemCount() == itemNames.size());
    }

    public int getCartItemCount() {
        return isVisible(cartBadge) ? Integer.parseInt(cartBadge.getText().trim()) : 0;
    }

    public void openCart() {
        click(cartLink);
    }
}
