// File: src/test/java/stepdefinitions/Hooks.java
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

    @BeforeAll
    public static void globalSetup() throws IOException {
        if (BaseClass.getDriver() == null) {
            BaseClass.initilizeBrowser();
            BaseClass.getDriver().manage().window().maximize();
        }
    }

    @AfterAll
    public static void globalTearDown() {
        BaseClass.quitDriver();
    }

    @AfterStep
    public void addScreenshot(Scenario scenario) {
        if (scenario.isFailed()) {
            WebDriver d = BaseClass.getDriver();
            if (d != null) {
                TakesScreenshot ts = (TakesScreenshot) d;
                byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", scenario.getName());
            }
        }
    }
}