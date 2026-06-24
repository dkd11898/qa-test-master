package mission.pages;

import mission.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CheckoutInfoPage extends BasePage {

    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    public CheckoutInfoPage() {
        PageFactory.initElements(driver, this);
    }

    public void fillCustomerInformation(String firstName, String lastName, String postalCode) {
        if (firstName != null) {
            firstNameField.clear();
            firstNameField.sendKeys(firstName);
        }
        if (lastName != null) {
            lastNameField.clear();
            lastNameField.sendKeys(lastName);
        }
        if (postalCode != null) {
            postalCodeField.clear();
            postalCodeField.sendKeys(postalCode);
        }
    }

    public void clickContinue() {
        continueButton.click();
    }
}
