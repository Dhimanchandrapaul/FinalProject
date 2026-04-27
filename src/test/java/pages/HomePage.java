package pages;

import Screenshot.projectSshot;
import base.BasePage;
import org.openqa.selenium.*;

public class HomePage extends BasePage {

    // ── Locators ─────────────────────────────────────────────────────────────────
    private static final By POPUP_CLOSE     = By.xpath("//div[@class='modal-body']/a");
    private static final By SEARCH_INPUT    = By.xpath("//input[@id='search']");
    private static final By GIFT_CARDS_LINK = By.xpath("//a[normalize-space()='GIFT CARDS']");

    // ── Constructor ──────────────────────────────────────────────────────────────
    public HomePage(WebDriver driver) {
        super(driver);
    }

    // ── Actions ──────────────────────────────────────────────────────────────────

    /** Opens the Pepperfry website and waits for page load. */
    public void open(String url) {
        driver.get(url);
        pause(5000);
        System.out.println("Opened: " + url);
    }

    /** Closes the popup if it appears on page load. */
    public void closePopupIfPresent() {
        try {
            WebElement closeBtn = driver.findElement(POPUP_CLOSE);
            jsClick(closeBtn);
            System.out.println("Popup closed.");
        } catch (Exception e) {
            System.out.println("No popup found.");
        }
    }

    /** Types keyword in the search box and hits ENTER. */
    public void searchFor(String keyword) {
        WebElement searchBox = waitForVisibility(SEARCH_INPUT);
        searchBox.sendKeys(keyword);
        actions.sendKeys(Keys.ENTER).perform();
        pause(3000);
        System.out.println("Searched for: " + keyword);

        // 📸 Screenshot #1 — BEFORE filter (search results page, no filters yet)
        projectSshot.capture(driver, "1_BeforeFilter");
    }

    /** Scrolls up and clicks the GIFT CARDS navigation link. */
    public void goToGiftCards() {
        pause(2000);
        scrollBy(0, -300);
        pause(2000);
        driver.findElement(GIFT_CARDS_LINK).click();
        pause(1000);
        System.out.println("Navigated to Gift Cards.");
    }
}
