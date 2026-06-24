package mission;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends BasePage {

    @FindBy(id = "user-name")
    private WebElement userNameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    public HomePage() {
        PageFactory.initElements(driver, this);
    }

    public void openHomePage() {
        driver.get(LoadProp.getProperty("url"));
    }

    public void login(String username, String password) {
        userNameField.clear();
        userNameField.sendKeys(username);
        passwordField.clear();
        passwordField.sendKeys(password);
        loginButton.click();
    }
}
