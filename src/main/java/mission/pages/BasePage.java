package mission.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Locale;
import java.util.function.Function;

public abstract class BasePage {

    private static final Duration DEFAULT_WAIT = Duration.ofSeconds(10);

    protected final WebDriver driver;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    protected void click(WebElement element) {
        waitUntilClickable(element).click();
    }

    protected void type(WebElement element, String text) {
        WebElement target = waitUntilVisible(element);
        target.clear();
        target.sendKeys(text);
    }

    protected boolean isVisible(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected WebElement waitUntilVisible(WebElement element) {
        return new WebDriverWait(driver, DEFAULT_WAIT).until(ExpectedConditions.visibilityOf(element));
    }

    protected WebElement waitUntilClickable(WebElement element) {
        return new WebDriverWait(driver, DEFAULT_WAIT).until(ExpectedConditions.elementToBeClickable(element));
    }

    protected WebElement waitUntilPresent(By locator) {
        return new WebDriverWait(driver, DEFAULT_WAIT).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected <T> T waitUntil(Function<WebDriver, T> condition) {
        return new WebDriverWait(driver, DEFAULT_WAIT).until(condition);
    }

    protected static String slugify(String itemName) {
        return itemName.toLowerCase(Locale.ENGLISH).replace(" ", "-");
    }
}
