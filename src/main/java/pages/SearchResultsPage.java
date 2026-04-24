package pages;

import base.BasePage;
import org.openqa.selenium.*;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsPage extends BasePage {

    // ── Locators ─────────────────────────────────────────────────────────────────
    private static final By MORE_FILTERS_BTN = By.xpath("//span[contains(@class,'more-filter')]");
    private static final By MAX_PRICE_INPUT  = By.xpath("//input[@formcontrolname='inputMax']");
    private static final By PRODUCT_NAMES    = By.xpath(
            "//h2[contains(@class,'product-name')] | //a[contains(@class,'product-title')]");
    private static final By PRODUCT_PRICES   = By.xpath(
            "//span[contains(@class,'offer-price')] | //span[contains(@class,'price-display')]");

    // ── Constructor ──────────────────────────────────────────────────────────────
    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    // ── Actions ──────────────────────────────────────────────────────────────────

    public void openMoreFilters() {
        WebElement btn = waitForClickable(MORE_FILTERS_BTN);
        jsClick(btn);
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
        WebElement maxInput = waitForPresence(MAX_PRICE_INPUT);
        scrollIntoView(maxInput);
        pause(1000);
        actions.moveToElement(maxInput)
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
    }

    /**
     * Prints the top N products (name + price) to console.
     * Used for quick console verification.
     */
    public void displayTopProducts(int count, int maxPrice) {
        pause(5000);
        scrollBy(0, 300);
        pause(2000);

        List<WebElement> names  = driver.findElements(PRODUCT_NAMES);
        List<WebElement> prices = driver.findElements(PRODUCT_PRICES);

        System.out.println("\n===============================================");
        System.out.println("Total products below ₹" + maxPrice + ": " + names.size());
        System.out.println("Displaying Top " + count + " products:");
        System.out.println("===============================================\n");

        int displayed = Math.min(count, names.size());
        for (int i = 0; i < displayed; i++) {
            String name  = names.get(i).getText();
            String price = (i < prices.size()) ? prices.get(i).getText() : "Price N/A";
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
        List<WebElement> names    = driver.findElements(PRODUCT_NAMES);
        List<WebElement> prices   = driver.findElements(PRODUCT_PRICES);

        System.out.println("Total products found: " + names.size());

        int limit = Math.min(count, names.size());

        for (int i = 0; i < limit; i++) {
            String name  = names.get(i).getText();
            String price = (i < prices.size()) ? prices.get(i).getText() : "0";
            if (!name.isEmpty() && !price.isEmpty()) {
                products.add(new String[]{name, price});
                System.out.println("Product: " + name + " | Price: " + price);
            }
        }
        return products;
    }
}