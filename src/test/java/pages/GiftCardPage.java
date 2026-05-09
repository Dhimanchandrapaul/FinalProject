package pages;

import Screenshot.projectSshot;
import factory.BaseClass;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class GiftCardPage extends BaseClass {

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

    @FindBy(xpath = "//span[contains(@class,'gc-den-card-value') and text()='1000']"
            + "/ancestor::div[contains(@class,'gc-den-card')]//a[text()='ADD']")
    private WebElement add1000Btn;

    @FindBy(xpath = "//button[@id='gc-proceed-checkout-btn']//div")
    private WebElement proceedBtn;

    @FindBy(xpath = "//div[contains(@class,'form-error')]")
    private WebElement formError;

    public GiftCardPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }


    public void selectBirthdayCard() {
        wait.until(ExpectedConditions.elementToBeClickable(birthdayCard));
        jsClick(birthdayCard);
        System.out.println("Birthday card clicked.");
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@formcontrolname='rname']")));
        pause(1000);
        System.out.println("Birthday card form loaded.");
    }

    public void fillRecipientName(String name) {
        wait.until(ExpectedConditions.visibilityOf(recipientName));
        recipientName.click();
        recipientName.clear();
        recipientName.sendKeys(name);
    }

    public void fillSenderName(String name) {
        wait.until(ExpectedConditions.visibilityOf(senderName));
        senderName.click();
        senderName.clear();
        senderName.sendKeys(name);
    }

    public void fillRecipientMobile(String mobile) {
        wait.until(ExpectedConditions.visibilityOf(recipientMobile));
        recipientMobile.click();
        recipientMobile.clear();
        recipientMobile.sendKeys(mobile);
    }

    public void fillSenderMobile(String mobile) {
        wait.until(ExpectedConditions.visibilityOf(senderMobile));
        senderMobile.click();
        senderMobile.clear();
        senderMobile.sendKeys(mobile);
    }

    public void fillSenderEmail(String email) {
        wait.until(ExpectedConditions.visibilityOf(senderEmail));
        senderEmail.click();
        senderEmail.clear();
        senderEmail.sendKeys(email);
    }

    public void fillMessage(String message) {
        wait.until(ExpectedConditions.visibilityOf(messageArea));
        messageArea.click();
        messageArea.clear();
        messageArea.sendKeys(message);
    }

    public void selectAmount1000() {
        wait.until(ExpectedConditions.visibilityOf(add1000Btn));
        scrollIntoView(add1000Btn);
        pause(1000);
        jsClick(add1000Btn);
        System.out.println("₹1000 denomination selected.");
    }

    public void clickProceedToCheckout() {
        jsClick(proceedBtn);
        pause(2000);
        System.out.println("Proceed to Checkout clicked.");
        try {
            wait.until(ExpectedConditions.visibilityOf(formError));
            scrollIntoView(formError);
            pause(500);
        } catch (Exception e) {
            System.out.println("No error popup appeared after Proceed click.");
        }
    }

    public String getFormErrorMessage() {
        wait.until(ExpectedConditions.visibilityOf(formError));
        scrollIntoView(formError);
        pause(500);
        projectSshot.capture(driver, "4_GiftCard_ErrorPopup");
        String error = formError.getText();
        System.out.println("Form error: " + error);
        return error;
    }

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