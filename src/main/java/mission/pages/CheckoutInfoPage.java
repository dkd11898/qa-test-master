package mission.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutInfoPage extends BasePage {

    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    public CheckoutInfoPage(WebDriver driver) {
        super(driver);
    }

    public void fillCustomerInformation(String firstName, String lastName, String postalCode) {
        if (firstName != null) {
            type(firstNameField, firstName);
        }
        if (lastName != null) {
            type(lastNameField, lastName);
        }
        if (postalCode != null) {
            type(postalCodeField, postalCode);
        }
    }

    public void clickContinue() {
        click(continueButton);
    }
}
