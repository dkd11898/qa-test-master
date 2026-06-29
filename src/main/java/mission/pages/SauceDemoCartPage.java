package mission.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class SauceDemoCartPage extends BasePage {

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    public SauceDemoCartPage(WebDriver driver) {
        super(driver);
    }

    public int getCartItemCount() {
        return cartItems.size();
    }

    public void verifyEachItemQuantityOne() {
        for (WebElement item : cartItems) {
            WebElement quantity = item.findElement(By.cssSelector(".cart_quantity"));
            if (!"1".equals(quantity.getText().trim())) {
                throw new AssertionError("Expected quantity 1 for each item but found " + quantity.getText());
            }
        }
    }

    public void removeItem(String itemName) {
        click(waitUntilPresent(By.id("remove-" + slugify(itemName))));
    }

    public void clickCheckout() {
        click(checkoutButton);
    }
}
