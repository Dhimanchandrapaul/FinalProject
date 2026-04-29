package pages;

import Screenshot.projectSshot;
import factory.BaseClass;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class GiftCardPage extends BaseClass {

    // ── Locators ─────────────────────────────────────────────────────────────────
    @FindBy(xpath = "//img[@alt='Pepperfry Birthday Gift Card']")
    private WebElement birthdayCard;

    @FindBy(xpath = "//input[@formcontrolname='rname']")
    private WebElement recipientName;

    @FindBy(xpath = "//input[@formcontrolname='sname']")
    private WebElement senderName;

    @FindBy(xpath = "//input[@formcontrolname='rmob']")
    private WebElement recipientMobile;

    @FindBy(xpath = "//input[@formcontrolname='smob']")
    private WebElement senderMobile;

    @FindBy(xpath = "//input[@formcontrolname='smail']")
    private WebElement senderEmail;

    @FindBy(xpath = "//textarea[@formcontrolname='rmsg']")
    private WebElement messageArea;

    @FindBy(xpath = "//span[contains(@class,'gc-den-card-value') and text()='1000']/ancestor::div[contains(@class,'gc-den-card')]//a[text()='ADD']")
    private WebElement add1000Btn;

    @FindBy(xpath = "//button[@id='gc-proceed-checkout-btn']//div")
    private WebElement proceedBtn;

    @FindBy(xpath = "//div[@class='form-error text-md ng-star-inserted']")
    private WebElement formError;

    // ── Constructor ──────────────────────────────────────────────────────────────
    public GiftCardPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // ── Individual Actions ────────────────────────────────────────────────────────

    /** Clicks the Birthday gift card image. */
    public void selectBirthdayCard() {
        actions.click(birthdayCard).perform();
        pause(2000);
        System.out.println("Birthday card selected.");
    }

    /** Fills the recipient name field. */
    public void fillRecipientName(String name) {
        wait.until(ExpectedConditions.visibilityOf(recipientName));
        recipientName.click();
        recipientName.clear();
        recipientName.sendKeys(name);
    }

    /** Fills the sender name field. */
    public void fillSenderName(String name) {
        wait.until(ExpectedConditions.visibilityOf(senderName));
        senderName.click();
        senderName.clear();
        senderName.sendKeys(name);
    }

    /** Fills the recipient mobile number field. */
    public void fillRecipientMobile(String mobile) {
        wait.until(ExpectedConditions.visibilityOf(recipientMobile));
        recipientMobile.click();
        recipientMobile.clear();
        recipientMobile.sendKeys(mobile);
    }

    /** Fills the sender mobile number field. */
    public void fillSenderMobile(String mobile) {
        wait.until(ExpectedConditions.visibilityOf(senderMobile));
        senderMobile.click();
        senderMobile.clear();
        senderMobile.sendKeys(mobile);
    }

    /** Fills the sender email field. */
    public void fillSenderEmail(String email) {
        wait.until(ExpectedConditions.visibilityOf(senderEmail));
        senderEmail.click();
        senderEmail.clear();
        senderEmail.sendKeys(email);
    }

    /** Fills the gift message textarea. */
    public void fillMessage(String message) {
        wait.until(ExpectedConditions.visibilityOf(messageArea));
        messageArea.click();
        messageArea.clear();
        messageArea.sendKeys(message);
    }

    /** Clicks ADD for the ₹1000 denomination card. */
    public void selectAmount1000() {
        wait.until(ExpectedConditions.visibilityOf(add1000Btn));
        scrollIntoView(add1000Btn);
        pause(1000);
        jsClick(add1000Btn);
        System.out.println("₹1000 denomination selected.");
    }

    /** Clicks the Proceed to Checkout button.
     *  Waits for error popup if present. */
    public void clickProceedToCheckout() {
        jsClick(proceedBtn);
        pause(2000);
        System.out.println("Proceed to Checkout clicked.");

        // Wait for error popup to render
        try {
            wait.until(ExpectedConditions.visibilityOf(formError));
            scrollIntoView(formError);
            pause(500);
        } catch (Exception e) {
            System.out.println("No error popup appeared after Proceed click.");
        }
    }

    /** Reads and returns the form validation error message.
     *  Also captures Screenshot #4 showing the gift card form + error popup. */
    public String getFormErrorMessage() {
        wait.until(ExpectedConditions.visibilityOf(formError));
        scrollIntoView(formError);
        pause(500);

        // 📸 Screenshot #4 — Gift card form with error popup visible
        projectSshot.capture(driver, "4_GiftCard_ErrorPopup");

        String error = formError.getText();
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