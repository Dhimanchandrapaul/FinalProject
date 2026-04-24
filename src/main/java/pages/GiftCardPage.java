package pages;

import Screenshot.projectSshot;
import base.BasePage;
import org.openqa.selenium.*;

public class GiftCardPage extends BasePage {

    // ── Locators ─────────────────────────────────────────────────────────────────
    private static final By BIRTHDAY_CARD    = By.xpath("//img[@alt='Pepperfry Birthday Gift Card']");
    private static final By RECIPIENT_NAME   = By.xpath("//input[@formcontrolname='rname']");
    private static final By SENDER_NAME      = By.xpath("//input[@formcontrolname='sname']");
    private static final By RECIPIENT_MOBILE = By.xpath("//input[@formcontrolname='rmob']");
    private static final By SENDER_MOBILE    = By.xpath("//input[@formcontrolname='smob']");
    private static final By SENDER_EMAIL     = By.xpath("//input[@formcontrolname='smail']");
    private static final By MESSAGE_AREA     = By.xpath("//textarea[@formcontrolname='rmsg']");
    private static final By ADD_1000_BTN     = By.xpath(
            "//span[contains(@class,'gc-den-card-value') and text()='1000']" +
                    "/ancestor::div[contains(@class,'gc-den-card')]//a[text()='ADD']");
    private static final By PROCEED_BTN      = By.xpath("//button[@id='gc-proceed-checkout-btn']//div");
    private static final By FORM_ERROR       = By.xpath(
            "//div[@class='form-error text-md ng-star-inserted']");

    // ── Constructor ──────────────────────────────────────────────────────────────
    public GiftCardPage(WebDriver driver) {
        super(driver);
    }

    // ── Individual Actions ────────────────────────────────────────────────────────

    /** Clicks the Birthday gift card image. */
    public void selectBirthdayCard() {
        WebElement card = driver.findElement(BIRTHDAY_CARD);
        actions.click(card).perform();
        pause(2000);
        System.out.println("Birthday card selected.");
    }

    /** Fills the recipient name field. */
    public void fillRecipientName(String name) {
        actions.sendKeys(driver.findElement(RECIPIENT_NAME), name).perform();
    }

    /** Fills the sender name field. */
    public void fillSenderName(String name) {
        actions.sendKeys(driver.findElement(SENDER_NAME), name).perform();
    }

    /** Fills the recipient mobile number field. */
    public void fillRecipientMobile(String mobile) {
        actions.sendKeys(driver.findElement(RECIPIENT_MOBILE), mobile).perform();
    }

    /** Fills the sender mobile number field. */
    public void fillSenderMobile(String mobile) {
        actions.sendKeys(driver.findElement(SENDER_MOBILE), mobile).perform();
    }

    /** Fills the sender email field. */
    public void fillSenderEmail(String email) {
        actions.sendKeys(driver.findElement(SENDER_EMAIL), email).perform();
    }

    /** Fills the gift message textarea. */
    public void fillMessage(String message) {
        actions.sendKeys(driver.findElement(MESSAGE_AREA), message).perform();
    }

    /** Clicks ADD for the ₹1000 denomination card. */
    public void selectAmount1000() {
        WebElement addBtn = waitForPresence(ADD_1000_BTN);
        scrollIntoView(addBtn);
        pause(1000);
        jsClick(addBtn);
        System.out.println("₹1000 denomination selected.");
    }

    /** Clicks the Proceed to Checkout button.
     *  Waits for error popup if present and takes screenshot. */
    public void clickProceedToCheckout() {
        WebElement btn = driver.findElement(PROCEED_BTN);
        jsClick(btn);
        pause(1000);
        System.out.println("Proceed to Checkout clicked.");

        // 📸 Screenshot — AFTER Proceed clicked (captures error popup if any)
        try {
            WebElement errorMsg = waitForVisibility(FORM_ERROR);
            scrollIntoView(errorMsg);
            pause(500);
        } catch (Exception e) {
            System.out.println("No error popup appeared after Proceed click.");
        }
        projectSshot.capture(driver, "4_AfterProceedClicked_ErrorPopup");
    }

    /** Reads and returns the form validation error message. */
    public String getFormErrorMessage() {
        String error = driver.findElement(FORM_ERROR).getText();
        System.out.println("Form error: " + error);
        return error;
    }

    // ── Composite Action ──────────────────────────────────────────────────────────

    /** Fills all gift card form fields in one call. */
    public void fillGiftCardForm(String recipientName, String senderName,
                                 String recipientMobile, String senderMobile,
                                 String senderEmail, String message) {
        fillRecipientName(recipientName);
        fillSenderName(senderName);
        fillRecipientMobile(recipientMobile);
        fillSenderMobile(senderMobile);
        fillSenderEmail(senderEmail);
        fillMessage(message);
        System.out.println("Gift card form filled.");
    }
}