package mission.pages;

import mission.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SauceDemoInventoryPage extends BasePage {

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartLink;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    public SauceDemoInventoryPage() {
        PageFactory.initElements(driver, this);
    }

    public void addItemsToCart(List<String> itemNames) {
        for (String itemName : itemNames) {
            String buttonId = "add-to-cart-" + itemName.toLowerCase(Locale.ENGLISH).replace(" ", "-");
            WebElement addButton = driver.findElement(By.id(buttonId));
            addButton.click();
        }
    }

    public int getCartItemCount() {
        if (isElementVisible(cartBadge)) {
            return Integer.parseInt(cartBadge.getText().trim());
        }
        return 0;
    }

    public void openCart() {
        cartLink.click();
    }

    public boolean isElementVisible(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
