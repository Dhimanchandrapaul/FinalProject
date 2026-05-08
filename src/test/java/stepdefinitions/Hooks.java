
package stepdefinitions;

import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import factory.BaseClass;
import io.cucumber.java.AfterAll;
import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;

public class Hooks {

    /**
     * FIX: @BeforeAll runs before Cucumber instantiates any glue class.
     * PepperfrySteps calls BaseClass.getDriver() in its constructor — if the
     * driver is null at that point the entire run fails with NullPointerException.
     *
     * Solution: initialize + configure the driver here, BEFORE step definitions
     * are constructed. Cucumber guarantees @BeforeAll fires first.
     */
    @BeforeAll
    public static void globalSetup() throws IOException {
        if (BaseClass.getDriver() == null) {
            WebDriver driver = BaseClass.initilizeBrowser();
            driver.manage().window().maximize();

            // Use only explicit waits (WebDriverWait) in page objects — no implicitlyWait.
            // Mixing implicit + explicit waits produces unpredictable timeout behaviour.
            java.time.Duration pageLoad = java.time.Duration.ofSeconds(60);
            java.time.Duration script   = java.time.Duration.ofSeconds(30);
            driver.manage().timeouts().pageLoadTimeout(pageLoad);
            driver.manage().timeouts().scriptTimeout(script);

            System.out.println("Browser initialized in @BeforeAll.");
        }
    }

    @AfterAll
    public static void globalTearDown() {
        BaseClass.quitDriver();
        // Save Excel report after all tests
        try {
            if (stepdefinitions.PepperfrySteps.reporter != null) {
                stepdefinitions.PepperfrySteps.reporter.save();
            }
        } catch (Exception e) {
            System.err.println("Failed to save Excel report: " + e.getMessage());
        }
    }

    /**
     * Attaches a screenshot to the Cucumber HTML report for every failed step.
     */
    @AfterStep
    public void addScreenshot(Scenario scenario) {
        if (scenario.isFailed()) {
            WebDriver d = BaseClass.getDriver();
            if (d != null) {
                byte[] screenshot = ((TakesScreenshot) d).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", scenario.getName());
            }
        }
    }
}