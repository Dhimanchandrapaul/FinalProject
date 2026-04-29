package pages;

import Screenshot.projectSshot;
import factory.BaseClass;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BaseClass {

    // ── Locators ─────────────────────────────────────────────────────────────────
    @FindBy(xpath = "//input[@id='search']")
    private WebElement searchInput;

    @FindBy(xpath = "//a[normalize-space()='GIFT CARDS']")
    private WebElement giftCardsLink;

    // ── Constructor ──────────────────────────────────────────────────────────────
    public HomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
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
            WebElement popupClose = driver.findElement(By.xpath("//div[@class='modal-body']/a"));
            jsClick(popupClose);
            System.out.println("Popup closed.");
        } catch (Exception e) {
            System.out.println("No popup found.");
        }
    }

    /** Types keyword in the search box and hits ENTER. */
    public void searchFor(String keyword) {
        wait.until(ExpectedConditions.visibilityOf(searchInput));
        searchInput.sendKeys(keyword);
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
        giftCardsLink.click();
        pause(1000);
        System.out.println("Navigated to Gift Cards.");
    }
}
