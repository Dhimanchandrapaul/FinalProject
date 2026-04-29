// File: src/test/java/factory/BaseClass.java
package factory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseClass {

    protected static WebDriver driver;
    private static Properties p;

    public static WebDriver initilizeBrowser() {
        if (driver == null) {
            driver = new ChromeDriver();
        }
        return driver;
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public static Properties getProperties() throws IOException {
        if (p == null) {
            FileInputStream file = new FileInputStream(
                    System.getProperty("user.dir") + "\\src\\test\\resources\\config.properties");
            p = new Properties();
            p.load(file);
        }
        return p;
    }

    protected WebDriverWait    wait;
    protected JavascriptExecutor js;
    protected Actions          actions;

    public BaseClass(WebDriver driver) {
        BaseClass.driver = driver;
        this.wait        = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js          = (JavascriptExecutor) driver;
        this.actions     = new Actions(driver);
    }

    public BaseClass() {}

    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void jsClick(WebElement element) {
        js.executeScript("arguments[0].click();", element);
    }

    protected void scrollIntoView(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    protected void scrollBy(int x, int y) {
        js.executeScript("window.scrollBy(" + x + "," + y + ");");
    }

    protected void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}