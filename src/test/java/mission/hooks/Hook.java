package mission.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import mission.config.ConfigReader;
import mission.driver.DriverFactory;
import mission.driver.DriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Date;

public class Hook {

    private static final Duration TIMEOUT = Duration.ofSeconds(20);

    @Before("@ui")
    public void initializeTest() {
        WebDriver driver = DriverFactory.createDriver(ConfigReader.getProperty("Browser"));
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(TIMEOUT);
        driver.manage().timeouts().implicitlyWait(TIMEOUT);
        driver.manage().timeouts().scriptTimeout(TIMEOUT);
        DriverManager.setDriver(driver);
    }

    @After("@ui")
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) {
            return;
        }
        try {
            captureScreenshot(driver, scenario);
        } finally {
            quietlyQuit(driver);
            DriverManager.unload();
        }
    }

    private void captureScreenshot(WebDriver driver, Scenario scenario) {
        try {
            String fileName = scenario.getName().replace(" ", "")
                    + new Timestamp(new Date().getTime()).toString().replaceAll("[^a-zA-Z0-9]", "")
                    + "_" + ConfigReader.getProperty("Browser") + ".jpg";
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(ConfigReader.getProperty("ScreenshotLocation") + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void quietlyQuit(WebDriver driver) {
        try {
            driver.quit();
        } catch (NoSuchSessionException ex) {
            // session already gone, ignore
        }
    }
}
