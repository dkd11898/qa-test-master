package mission.pages;

import mission.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class SauceDemoCartPage extends BasePage {

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    public SauceDemoCartPage() {
        PageFactory.initElements(driver, this);
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
        WebElement itemRow = driver.findElement(By.xpath("//div[@class='cart_item']//*[text()='" + itemName + "']/ancestor::div[@class='cart_item']"));
        WebElement removeButton = itemRow.findElement(By.cssSelector("button.cart_button"));
        removeButton.click();
    }

    public void clickCheckout() {
        checkoutButton.click();
    }
}
