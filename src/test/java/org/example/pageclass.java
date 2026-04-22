package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class pageclass {

    // 🔧 Configurable test data
    static final String WEBSITE_URL = "https://www.pepperfry.com/";
    static final String SEARCH_KEYWORD = "Bookshelves";
    static final String BRAND_FILTER = "Brand";
    static final String BRAND_NAME = "WoodenMood";
    static final String FILTER_NAME = "Price";
    static final int MAX_PRICE = 15000;
    static final String APPLY_BUTTON_TEXT = "APPLY";
    static final int TOP_N = 3;



    public static void main(String[] args) throws InterruptedException {

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Actions actions = new Actions(driver);

        // 1. Open website
        driver.get(WEBSITE_URL);
        Thread.sleep(10000);

        // 2. Close popup
        closePopup(driver, js);

        // 3. Search using dynamic keyword
        searchProduct(driver, wait, actions, SEARCH_KEYWORD);

        // 4. Open More Filters
        clickElement(driver, wait, "//span[contains(@class,'more-filter')]");

        // 5. Expand filter by dynamic name
        expandFilterByName(driver, wait, js, FILTER_NAME);

        // 6. Set dynamic max price
        setMaxPrice(driver, wait, js, actions, MAX_PRICE);
        expandFilterByName(driver, wait, js, BRAND_FILTER);
        selectBrand(driver, wait, js, BRAND_NAME);
        // 7. Click Apply using dynamic button text
        clickButtonByText(driver, wait, js, APPLY_BUTTON_TEXT);

        System.out.println("All filters applied dynamically");
        Thread.sleep(5000);
        displayTopProducts(driver, js, TOP_N);
        Thread.sleep(5000);

        System.out.println("URL: " + driver.getCurrentUrl());

        //call gift card method
        GiftCard(driver,js,actions);

    }



    // ======================= DYNAMIC METHODS =======================

    public static void closePopup(WebDriver driver, JavascriptExecutor js) {
        try {
            WebElement cls = driver.findElement(By.xpath("//div[@class='modal-body']/a"));
            js.executeScript("arguments[0].click()", cls);
            System.out.println("Popup closed");
        } catch (Exception e) {
            System.out.println("No popup");
        }
    }

    // 🔧 Dynamic: search for any keyword
    public static void searchProduct(WebDriver driver, WebDriverWait wait, Actions actions, String keyword) throws InterruptedException {
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@id='search']")
        ));
        searchInput.sendKeys(keyword);
        actions.sendKeys(Keys.ENTER).perform();
        Thread.sleep(5000);
        System.out.println("Searched: " + keyword);
    }

    // 🔧 Dynamic: click element by xpath
    public static void clickElement(WebDriver driver, WebDriverWait wait, String xpath) throws InterruptedException {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        el.click();
        Thread.sleep(2000);
    }

    // 🔧 Dynamic: expand filter by name
    public static void expandFilterByName(WebDriver driver, WebDriverWait wait, JavascriptExecutor js, String filterName) throws InterruptedException {
        String xpath = String.format("//accordion-heading[normalize-space()='%s']", filterName);
        WebElement filter = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", filter);
        Thread.sleep(1000);
        filter.click();
        Thread.sleep(1500);
        System.out.println("Expanded filter: " + filterName);
    }

    // 🔧 Dynamic: set max price to any value
    public static void setMaxPrice(WebDriver driver, WebDriverWait wait, JavascriptExecutor js, Actions actions, int maxPrice) throws InterruptedException {
        WebElement maxInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@formcontrolname='inputMax']")
        ));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", maxInput);
        Thread.sleep(1000);

        actions.moveToElement(maxInput)
                .click()
                .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                .sendKeys(Keys.DELETE)
                .sendKeys(String.valueOf(maxPrice))
                .sendKeys(Keys.TAB)
                .perform();

        Thread.sleep(1500);
        System.out.println("Max price set to: " + maxPrice);
    }
    public static void selectBrand(WebDriver driver, WebDriverWait wait, JavascriptExecutor js, String brandName) throws InterruptedException {
        // ✅ Click the label - labels toggle checkboxes when clicked
        String xpath = String.format("//label[@for='%s']", brandName);

        WebElement brand = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", brand);
        Thread.sleep(1000);
        js.executeScript("arguments[0].click();", brand);
        Thread.sleep(2000);
        System.out.println("✅ Selected brand: " + brandName);
    }
    // 🔧 Dynamic: click any button by visible text
    public static void clickButtonByText(WebDriver driver, WebDriverWait wait, JavascriptExecutor js, String buttonText) throws InterruptedException {
        String xpath = String.format("//button[normalize-space()='%s']", buttonText);
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
        Thread.sleep(500);
        js.executeScript("arguments[0].click();", btn);
        Thread.sleep(3000);
        System.out.println("Clicked button: " + buttonText);
    }
    public static void displayTopProducts(WebDriver driver, JavascriptExecutor js, int count) throws InterruptedException {
        Thread.sleep(5000);
        js.executeScript("window.scrollBy(0, 300);");
        Thread.sleep(2000);

        // Try multiple locator patterns for Pepperfry
        List<WebElement> names = driver.findElements(
                By.xpath("//h2[contains(@class,'product-name')] | //a[contains(@class,'product-title')]")
        );
        List<WebElement> prices = driver.findElements(
                By.xpath("//span[contains(@class,'offer-price')] | //span[contains(@class,'price-display')]")
        );

        System.out.println("\n===============================================");
        System.out.println("Total Bookshelves below ₹" + MAX_PRICE + ": " + names.size());
        System.out.println("Displaying Top " + count + " products:");
        System.out.println("===============================================\n");

        int displayed = Math.min(count, names.size());
        for (int i = 0; i < displayed; i++) {
            String name = names.get(i).getText();
            String price = i < prices.size() ? prices.get(i).getText() : "Price N/A";

            System.out.println((i + 1) + ". " + name);
            System.out.println("   Price: " + price);
            System.out.println("-----------------------------------------------");
        }
    }

    public static void GiftCard(WebDriver driver,JavascriptExecutor js,Actions actions) throws InterruptedException {

        Thread.sleep(2000);
        js.executeScript("window.scrollBy(0,-300);");
        Thread.sleep(2000);
        driver.findElement(By.xpath("//a[normalize-space()='GIFT CARDS']")).click();


        Thread.sleep(1000);
        WebElement birthdayCard=driver.findElement(By.xpath("//img[@alt='Pepperfry Birthday Gift Card']"));
        actions.click(birthdayCard).perform();
        Thread.sleep(2000);


        WebElement rName = driver.findElement(By.xpath("//input[@formcontrolname='rname']"));
//        actions.click(sName).perform();
        actions.sendKeys(rName, "nishank").perform();
        WebElement sName = driver.findElement(By.xpath("//input[@formcontrolname='sname']"));
        actions.sendKeys(sName, "Dhiman").perform();

        WebElement rNo=driver.findElement(By.xpath("//input[@formcontrolname='rmob']"));
        actions.sendKeys(rNo, "9999999999").perform();
        WebElement sNo=driver.findElement(By.xpath("//input[@formcontrolname='smob']"));
        actions.sendKeys(sNo, "5555555555").perform();

        WebElement sMail=driver.findElement(By.xpath("//input[@formcontrolname='smail']"));
        actions.sendKeys(sMail, "Dhiman@gmail.com").perform();

        WebElement msg=driver.findElement(By.xpath("//textarea[@formcontrolname='rmsg']"));
        actions.sendKeys(msg, "Happy Birthday! Enjoy shopping").perform();

//        driver.findElement(By.xpath("//span[@class='gc-den-card-value' and text()='1000']/ancestor::div[@class='gc-den-card']//a[text()='ADD']")).click();
        driver.findElement(By.xpath("//span[contains(@class,'gc-den-card-value') and text()='1000']/ancestor::div[contains(@class,'gc-den-card')]//a[text()='ADD']")).click();

//        WebElement btn=driver.findElement(By.id("gc-proceed-checkout-btn"));
//        actions.click(btn).perform();

//button[@id='gc-proceed-checkout-btn' and .

      WebElement btn=driver.findElement(By.xpath("//button[@id='gc-proceed-checkout-btn']//div"));
        js.executeScript("arguments[0].click();", btn);


        // Get text
        Thread.sleep(1000);
        String errorText = driver.findElement(By.xpath(
                "//div[@class='form-error text-md ng-star-inserted']")).getText();
        System.out.println("Error msg: "+errorText);



    }
}
