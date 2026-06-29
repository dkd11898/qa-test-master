package mission.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Locale;

public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver(String browser) {
        switch (browser.toLowerCase(Locale.ENGLISH)) {
            case "chrome":
            case "chromemac":
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver();
            case "edge":
                WebDriverManager.edgedriver().setup();
                return new EdgeDriver();
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver();
            case "chromeheadless":
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless=new", "--window-size=1920,1080");
                return new ChromeDriver(options);
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }
}
