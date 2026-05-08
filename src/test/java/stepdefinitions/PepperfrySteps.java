package stepdefinitions;

import factory.BaseClass;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import pages.GiftCardPage;
import pages.HomePage;
import pages.SearchResultsPage;
import utils.ExcelReporter;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.*;

public class PepperfrySteps {

    private WebDriver         driver;
    private Properties        p;
    private HomePage          homePage;
    private SearchResultsPage resultsPage;
    private GiftCardPage      giftPage;
    private ExcelReporter     reporter;
    private List<String[]>    products;

    public PepperfrySteps() throws IOException {
        this.driver      = BaseClass.getDriver();
        this.p           = BaseClass.getProperties();
        this.homePage    = new HomePage(driver);
        this.resultsPage = new SearchResultsPage(driver);
        this.giftPage    = new GiftCardPage(driver);
        this.reporter    = new ExcelReporter("Resources/GiftCardTestData.xlsx");
    }

    // ── Scenario 1: Home Decor Hover ──────────────────────────────────────────────

    @Given("the user launches the Pepperfry website")
    public void the_user_launches_the_pepperfry_website() {
        homePage.open(p.getProperty("website.url"));
    }

    @And("any popup on the page is closed")
    public void any_popup_on_the_page_is_closed() {
        homePage.closePopupIfPresent();
    }

    @When("the user hovers over the Home Decor menu")
    public void the_user_hovers_over_the_home_decor_menu() {
        WebElement homeDecorMenu = driver.findElement(
                By.xpath("//a[contains(text(),'Home Decor')]"));
        new Actions(driver).moveToElement(homeDecorMenu).perform();
        homePage.pause(2000);
    }

    @Then("the Home Decor menu items should be captured and printed")
    public void the_home_decor_menu_items_should_be_captured_and_printed() {
        List<WebElement> menuItems = driver.findElements(
                By.xpath("//div[contains(@class,'dropdown')]//a | //ul[contains(@class,'sub-menu')]//a"));
        assertFalse(menuItems.isEmpty(), "No Home Decor menu items found after hover.");
        System.out.println("Home Decor Menu Items:");
        for (WebElement item : menuItems) {
            String text = item.getText().trim();
            if (!text.isEmpty()) {
                System.out.println("  - " + text);
            }
        }
    }

    // ── Scenario 2: Search + Filter ───────────────────────────────────────────────

    @Given("the user opens the Pepperfry website")
    public void the_user_opens_the_pepperfry_website() {
        homePage.open(p.getProperty("website.url"));
    }

    @And("the user closes the popup if present")
    public void the_user_closes_the_popup_if_present() {
        homePage.closePopupIfPresent();
    }

    @When("the user searches for {string}")
    public void the_user_searches_for(String keyword) {
        homePage.searchFor(keyword);
    }

    @And("the user applies a maximum price filter of {int} and selects the brand {string}")
    public void the_user_applies_a_maximum_price_filter_and_selects_brand(int maxPrice, String brand) {
        resultsPage.openMoreFilters();
        resultsPage.expandFilter(p.getProperty("price.filter"));
        resultsPage.setMaxPrice(maxPrice);
        resultsPage.expandFilter(p.getProperty("brand.filter"));
        resultsPage.selectBrand(brand);
        resultsPage.clickApply(p.getProperty("apply.button"));
        homePage.pause(2000);
    }

    @And("the top 3 products with their prices should be displayed")
    public void the_top_3_products_with_their_prices_should_be_displayed() {
        products = resultsPage.getAllProductNamesAndPrices(Integer.parseInt(p.getProperty("top.n")));
        assertFalse(products.isEmpty(), "No products found after applying filters.");
        System.out.println("Top 3 Products:");
        for (String[] prod : products) {
            System.out.println("  Product: " + prod[0] + " | Price: Rs." + prod[1]);
        }
    }

    @Then("the user validates that prices are less than {int}")
    public void the_user_validates_that_prices_are_less_than(int maxPrice) {
        assertFalse(products == null || products.isEmpty(), "No products to validate.");
        for (String[] prod : products) {
            int price = Integer.parseInt(prod[1].replaceAll("[^0-9]", ""));
            assertTrue(price < maxPrice,
                    "Product '" + prod[0] + "' price Rs." + price + " is NOT below Rs." + maxPrice);
        }
    }

    // ── Scenario 3: Gift Card Sender Email Validation ─────────────────────────────

    @Given("the user navigates to the Pepperfry home page")
    public void the_user_navigates_to_the_pepperfry_home_page() {
        homePage.open(p.getProperty("website.url"));
    }

    @And("any visible popup on the home page is closed")
    public void any_visible_popup_on_the_home_page_is_closed() {
        homePage.closePopupIfPresent();
    }

    @When("the user opens gift card option")
    public void the_user_opens_gift_card_option() {
        homePage.goToGiftCards();
    }

    @And("the user selects the birthday gift card")
    public void the_user_selects_the_birthday_gift_card() {
        giftPage.selectBirthdayCard();
    }

    @And("the user enters gift card details from excel row {string}")
    @And("the user enters gift card details from the properties file")
    public void the_user_enters_gift_card_details_from_properties_file() {
        giftPage.fillGiftCardForm(
                p.getProperty("gc.recipient.name"),
                p.getProperty("gc.sender.name"),
                p.getProperty("gc.recipient.mobile"),
                p.getProperty("gc.sender.mobile"),
                p.getProperty("gc.sender.email"),
                p.getProperty("gc.message")
        );
    }

    @And("the user selects the 1000 denomination")
    public void the_user_selects_the_1000_denomination() {
        giftPage.selectAmount1000();
    }

    @And("the user clicks proceed to checkout")
    public void the_user_clicks_proceed_to_checkout() {
        giftPage.clickProceedToCheckout();
    }

    @Then("the sender email validation message should be displayed correctly")
    public void the_sender_email_validation_message_should_be_displayed_correctly() {
        String error = giftPage.getFormErrorMessage();
        assertFalse(error == null || error.trim().isEmpty(),
                "Expected a sender email validation message but none was displayed.");
        System.out.println("Validation message: " + error.trim());
    }
}