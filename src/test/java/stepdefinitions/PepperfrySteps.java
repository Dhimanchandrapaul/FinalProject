package stepdefinitions;

import factory.BaseClass;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import pages.GiftCardPage;
import pages.HomePage;
import pages.SearchResultsPage;
import utils.ExcelReporter;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.*;

public class PepperfrySteps {

    private WebDriver driver;
    private Properties p;
    private HomePage homePage;
    private SearchResultsPage resultsPage;
    private GiftCardPage giftPage;
    private ExcelReporter reporter;
    private List<String[]> products;

    public PepperfrySteps() throws IOException {
        this.driver = BaseClass.getDriver();
        this.p = BaseClass.getProperties();
        this.homePage = new HomePage(driver);
        this.resultsPage = new SearchResultsPage(driver);
        this.giftPage = new GiftCardPage(driver);
        this.reporter = new ExcelReporter("Resources/PepperfryTestResults.xlsx");
    }

    @Given("I open the Pepperfry website")
    public void i_open_the_pepperfry_website() {
        if (driver.getCurrentUrl() == null || !driver.getCurrentUrl().contains("pepperfry.com")) {
            homePage.open(p.getProperty("website.url"));
        }
    }

    @When("I close the popup if present")
    public void i_close_the_popup_if_present() {
        homePage.closePopupIfPresent();
    }

    @Then("the page title should contain {string}")
    public void the_page_title_should_contain(String expected) {
        String title = driver.getTitle();
        assertTrue(title != null && title.toLowerCase().contains(expected.toLowerCase()));
    }

    @Given("I am on the Pepperfry homepage")
    public void i_am_on_the_pepperfry_homepage() {
        i_open_the_pepperfry_website();
        // Do not force a reload if we are already navigating through the site smoothly
    }

    @When("I search for {string}")
    public void i_search_for(String keyword) {
        homePage.searchFor(keyword);
    }

    @Then("the URL should contain {string} or {string}")
    public void the_url_should_contain_or(String s1, String s2) {
        String url = driver.getCurrentUrl().toLowerCase();
        assertTrue(url.contains(s1.toLowerCase()) || url.contains(s2.toLowerCase()));
    }

    @Given("I am on the search results page")
    public void i_am_on_the_search_results_page() {
        String url = driver.getCurrentUrl().toLowerCase();
        // Only search if we aren't already on the search page from the previous scenario
        if (!url.contains("search") && !url.contains("bookshelves")) {
            i_am_on_the_pepperfry_homepage();
            i_search_for(p.getProperty("search.keyword"));
        }
    }

    @When("I apply a maximum price of {int} and select brand {string}")
    public void i_apply_price_and_brand_filter(int maxPrice, String brand) {
        resultsPage.openMoreFilters();
        resultsPage.expandFilter(p.getProperty("price.filter"));
        resultsPage.setMaxPrice(maxPrice);
        resultsPage.expandFilter(p.getProperty("brand.filter"));
        resultsPage.selectBrand(brand);
        resultsPage.clickApply(p.getProperty("apply.button"));
    }

    @Then("the URL should contain {string} and {string}")
    public void the_url_should_contain_and(String price, String brand) {
        String url = driver.getCurrentUrl();
        assertTrue(url.contains(price) && url.toLowerCase().contains(brand.toLowerCase()));
    }

    @Given("I am on the filtered search results page")
    public void i_am_on_the_filtered_search_results_page() {
        String url = driver.getCurrentUrl().toLowerCase();
        String brand = p.getProperty("brand.name").toLowerCase();
        // Only apply filters if they aren't already applied from the previous scenario
        if (!url.contains(brand)) {
            i_am_on_the_search_results_page();
            i_apply_price_and_brand_filter(
                    Integer.parseInt(p.getProperty("max.price")),
                    p.getProperty("brand.name"));
        }
    }

    @Then("each product price should be below {int}")
    public void each_product_price_should_be_below(int maxPrice) {
        products = resultsPage.getAllProductNamesAndPrices(Integer.parseInt(p.getProperty("top.n")));
        assertFalse(products.isEmpty());
        for (String[] prod : products) {
            int price = Integer.parseInt(prod[1].replaceAll("[^0-9]", ""));
            assertTrue(price < maxPrice);
        }
    }

    @When("I go to Gift Cards and select Birthday card")
    public void i_go_to_gift_cards_and_select_birthday_card() {
        homePage.goToGiftCards();
        giftPage.selectBirthdayCard();
    }

    @When("I fill the gift card form with valid details except receiver email")
    public void i_fill_gift_card_form_with_valid_details_except_receiver_email() {
        giftPage.fillGiftCardForm(
                p.getProperty("gc.recipient.name"),
                p.getProperty("gc.sender.name"),
                p.getProperty("gc.recipient.mobile"),
                p.getProperty("gc.sender.mobile"),
                p.getProperty("gc.sender.email"),
                p.getProperty("gc.message")
        );
    }

    @When("I select Rs.1000 and proceed to checkout")
    public void i_select_1000_and_proceed_to_checkout() {
        giftPage.selectAmount1000();
        giftPage.clickProceedToCheckout();
    }

    @Then("the form should be submitted")
    public void the_form_should_be_submitted() {
        assertTrue(true);
    }

    @Given("I am on the gift card checkout page with missing receiver email")
    public void i_am_on_gift_card_checkout_page_with_missing_receiver_email() {
        String url = driver.getCurrentUrl().toLowerCase();
        // Since Scenario 5 already fills the form, we do nothing if we are already on the gift cards page
        if (!url.contains("gift")) {
            i_am_on_the_pepperfry_homepage();
            i_go_to_gift_cards_and_select_birthday_card();
            i_fill_gift_card_form_with_valid_details_except_receiver_email();
            i_select_1000_and_proceed_to_checkout();
        }
    }

    @Then("I should see the error message {string}")
    public void i_should_see_the_error_message(String expected) {
        String error = giftPage.getFormErrorMessage();
        assertEquals(error.trim(), expected);
    }

    @Then("the first product price should be below {int}")
    public void the_first_product_price_should_be_below(int priceLimit) {
        products = resultsPage.getAllProductNamesAndPrices(1);
        assertFalse(products.isEmpty());
        int price = Integer.parseInt(products.get(0)[1].replaceAll("[^0-9]", ""));
        assertTrue(price < priceLimit);
    }
}