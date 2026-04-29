package pages;

import Screenshot.projectSshot;
import factory.BaseClass;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsPage extends BaseClass {

    // ── Locators ─────────────────────────────────────────────────────────────────
    @FindBy(xpath = "//span[contains(@class,'more-filter')]")
    private WebElement moreFiltersBtn;

    @FindBy(xpath = "//input[@formcontrolname='inputMax']")
    private WebElement maxPriceInput;

    @FindBy(xpath = "//h2[contains(@class,'product-name')] | //a[contains(@class,'product-title')]")
    private List<WebElement> productNames;

    @FindBy(xpath = "//span[contains(@class,'offer-price')] | //span[contains(@class,'price-display')]")
    private List<WebElement> productPrices;

    // Ensures screenshot #3 fires only once per run (skips tc007's second call)
    private boolean topProductsCaptured = false;

    // ── Constructor ──────────────────────────────────────────────────────────────
    public SearchResultsPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // ── Actions ──────────────────────────────────────────────────────────────────

    public void openMoreFilters() {
        wait.until(ExpectedConditions.elementToBeClickable(moreFiltersBtn));
        jsClick(moreFiltersBtn);
        pause(2000);
        System.out.println("More Filters panel opened.");
    }

    public void expandFilter(String filterName) {
        By locator = By.xpath(
                String.format("//accordion-heading[normalize-space()='%s']", filterName));
        WebElement filter = waitForPresence(locator);
        scrollIntoView(filter);
        pause(1000);
        filter.click();
        pause(1500);
        System.out.println("Expanded filter: " + filterName);
    }

    public void setMaxPrice(int maxPrice) {
        wait.until(ExpectedConditions.visibilityOf(maxPriceInput));
        scrollIntoView(maxPriceInput);
        pause(1000);
        actions.moveToElement(maxPriceInput)
                .click()
                .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                .sendKeys(Keys.DELETE)
                .sendKeys(String.valueOf(maxPrice))
                .sendKeys(Keys.TAB)
                .perform();
        pause(1500);
        System.out.println("Max price set to: ₹" + maxPrice);
    }

    public void selectBrand(String brandName) {
        By locator = By.xpath(String.format("//label[@for='%s']", brandName));
        WebElement brand = waitForPresence(locator);
        scrollIntoView(brand);
        pause(1000);
        jsClick(brand);
        pause(2000);
        System.out.println("Selected brand: " + brandName);
    }

    public void clickApply(String buttonText) {
        By locator = By.xpath(String.format("//button[normalize-space()='%s']", buttonText));
        WebElement btn = waitForClickable(locator);
        scrollIntoView(btn);
        pause(500);
        jsClick(btn);
        pause(3000);
        System.out.println("Clicked: " + buttonText);

        // 📸 Screenshot #2 — AFTER applying filter
        projectSshot.capture(driver, "2_AfterFilter");
    }

    /**
     * Prints the top N products (name + price) to console.
     * Used for quick console verification.
     */
    public void displayTopProducts(int count, int maxPrice) {
        pause(5000);
        scrollBy(0, 300);
        pause(2000);

        System.out.println("\n===============================================");
        System.out.println("Total products below ₹" + maxPrice + ": " + productNames.size());
        System.out.println("Displaying Top " + count + " products:");
        System.out.println("===============================================\n");

        int displayed = Math.min(count, productNames.size());
        for (int i = 0; i < displayed; i++) {
            String name  = productNames.get(i).getText();
            String price = (i < productPrices.size()) ? productPrices.get(i).getText() : "Price N/A";
            System.out.println((i + 1) + ". " + name);
            System.out.println("   Price: " + price);
            System.out.println("-----------------------------------------------");
        }
    }

    /**
     * Returns first `count` product names and prices from results page.
     * Each String[] has [0] = product name, [1] = price text.
     * Used for Excel reporting.
     */
    public List<String[]> getAllProductNamesAndPrices(int count) {
        pause(2000);
        scrollBy(0, 300);
        pause(1000);

        List<String[]>   products = new ArrayList<>();

        System.out.println("Total products found: " + productNames.size());

        int limit = Math.min(count, productNames.size());

        for (int i = 0; i < limit; i++) {
            String name  = productNames.get(i).getText();
            String price = (i < productPrices.size()) ? productPrices.get(i).getText() : "0";
            if (!name.isEmpty() && !price.isEmpty()) {
                products.add(new String[]{name, price});
                System.out.println("Product: " + name + " | Price: " + price);
            }
        }

        // 📸 Screenshot #3 — TOP PRODUCTS (only on first call, skip later duplicates)
        if (!topProductsCaptured) {
            projectSshot.capture(driver, "3_TopProductsBelow");
            topProductsCaptured = true;
        }

        return products;
    }
}
