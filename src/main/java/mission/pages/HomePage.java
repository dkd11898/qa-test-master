package mission.pages;

import mission.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    @FindBy(id = "user-name")
    private WebElement userNameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void openHomePage() {
        driver.get(ConfigReader.getProperty("ui.baseUrl"));
    }

    public void login(String username, String password) {
        type(userNameField, username);
        type(passwordField, password);
        click(loginButton);
    }
}
