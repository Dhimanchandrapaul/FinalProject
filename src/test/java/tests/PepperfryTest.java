package tests;

import factory.BaseClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.GiftCardPage;
import pages.HomePage;
import pages.SearchResultsPage;
import utils.ExcelReporter;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class PepperfryTest {

    WebDriver         driver;
    Properties        p;
    HomePage          homePage;
    SearchResultsPage resultsPage;
    GiftCardPage      giftPage;
    ExcelReporter     reporter;
    int               serial = 1;

    @BeforeClass
    public void setUp() throws Exception {
        reporter = new ExcelReporter("Resources/PepperfryTestResults.xlsx");

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));

        p = BaseClass.getProperties();

        homePage    = new HomePage(driver);
        resultsPage = new SearchResultsPage(driver);
        giftPage    = new GiftCardPage(driver);
    }

    @AfterClass
    public void tearDown() {
        if (reporter != null) {
            reporter.save();
        }
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(priority = 1)
    public void tc001_OpenWebsite() {
        try {
            homePage.open(p.getProperty("website.url"));
            homePage.closePopupIfPresent();
            String  title = driver.getTitle();
            boolean pass  = title != null && title.toLowerCase().contains("pepperfry");
            log("TC_001", "Open Website",
                    "Open pepperfry.com and close popup",
                    "Title contains 'Pepperfry'",
                    "Title: " + title,
                    pass ? "PASS" : "FAIL");
        } catch (Exception e) {
            log("TC_001", "Open Website",
                    "Open pepperfry.com and close popup",
                    "Title contains 'Pepperfry'",
                    "Exception: " + e.getMessage(), "FAIL");
        }
    }

    @Test(priority = 2)
    public void tc002_SearchBookshelves() {
        try {
            homePage.searchFor(p.getProperty("search.keyword"));
            String  url  = driver.getCurrentUrl();
            boolean pass = url.toLowerCase().contains("search") ||
                    url.toLowerCase().contains("bookshelves");
            log("TC_002", "Search Bookshelves",
                    "Type 'Bookshelves' in search box and press ENTER",
                    "URL contains 'search' or 'bookshelves'",
                    "URL: " + url,
                    pass ? "PASS" : "FAIL");
        } catch (Exception e) {
            log("TC_002", "Search Bookshelves",
                    "Type 'Bookshelves' in search box and press ENTER",
                    "URL contains 'search' or 'bookshelves'",
                    "Exception: " + e.getMessage(), "FAIL");
        }
    }

    @Test(priority = 3)
    public void tc003_ApplyPriceAndBrandFilter() {
        int    maxPrice  = Integer.parseInt(p.getProperty("max.price"));
        String brandName = p.getProperty("brand.name");
        try {
            resultsPage.openMoreFilters();
            resultsPage.expandFilter(p.getProperty("price.filter"));
            resultsPage.setMaxPrice(maxPrice);
            resultsPage.expandFilter(p.getProperty("brand.filter"));
            resultsPage.selectBrand(brandName);
            resultsPage.clickApply(p.getProperty("apply.button"));
            String  url      = driver.getCurrentUrl();
            boolean hasPrice = url.contains(String.valueOf(maxPrice));
            boolean hasBrand = url.toLowerCase().contains(brandName.toLowerCase());
            boolean pass     = hasPrice && hasBrand;
            log("TC_003", "Apply Price + Brand Filter",
                    "Set max price Rs." + maxPrice + " → Select brand '" + brandName + "' → Click APPLY",
                    "URL contains Rs." + maxPrice + " and '" + brandName + "'",
                    "URL: " + url,
                    pass ? "PASS" : "FAIL");
        } catch (Exception e) {
            log("TC_003", "Apply Price + Brand Filter",
                    "Set max price Rs." + maxPrice + " → Select brand '" + brandName + "' → Click APPLY",
                    "URL contains Rs." + maxPrice + " and '" + brandName + "'",
                    "Exception: " + e.getMessage(), "FAIL");
        }
    }

    @Test(priority = 4)
    public void tc004_VerifyAllProductPrices() {
        int maxPrice = Integer.parseInt(p.getProperty("max.price"));
        int topN     = Integer.parseInt(p.getProperty("top.n"));
        try {
            List<String[]> products = resultsPage.getAllProductNamesAndPrices(topN);

            if (products.isEmpty()) {
                log("TC_004", "Verify Product Price < Rs." + maxPrice,
                        "Check each product price is below Rs." + maxPrice,
                        "At least 1 product below Rs." + maxPrice,
                        "No products found on page", "FAIL");
                return;
            }

            for (int i = 0; i < products.size(); i++) {
                String  tcId     = String.format("TC_%03d", serial);
                String  name     = products.get(i)[0];
                int     price    = Integer.parseInt(products.get(i)[1].replaceAll("[^0-9]", ""));
                boolean pass     = price < maxPrice;
                log(tcId, "Verify Product Price < Rs." + maxPrice,
                        "Check price of: " + name,
                        "Price < Rs." + maxPrice,
                        "Actual Price: Rs." + price,
                        pass ? "PASS" : "FAIL");
            }

        } catch (Exception e) {
            log("TC_004", "Verify Product Price < Rs." + maxPrice,
                    "Check each product price is below Rs." + maxPrice,
                    "All prices < Rs." + maxPrice,
                    "Exception: " + e.getMessage(), "FAIL");
        }
    }

    @Test(priority = 5)
    public void tc005_FillGiftCardForm() {
        try {
            homePage.goToGiftCards();
            giftPage.selectBirthdayCard();
            giftPage.fillGiftCardForm(
                    p.getProperty("gc.recipient.name"),
                    p.getProperty("gc.sender.name"),
                    p.getProperty("gc.recipient.mobile"),
                    p.getProperty("gc.sender.mobile"),
                    p.getProperty("gc.sender.email"),
                    p.getProperty("gc.message"));
            giftPage.selectAmount1000();
            giftPage.clickProceedToCheckout();
            log("TC_005", "Fill Gift Card Form",
                    "Go to Gift Cards → Birthday card → fill form → Rs.1000 → Proceed",
                    "Form filled and Proceed to Checkout clicked",
                    "Recipient: " + p.getProperty("gc.recipient.name") +
                            " | Sender: " + p.getProperty("gc.sender.name") +
                            " | Rs.1000 selected | Proceed clicked", "PASS");
        } catch (Exception e) {
            log("TC_005", "Fill Gift Card Form",
                    "Go to Gift Cards → Birthday card → fill form → Rs.1000 → Proceed",
                    "Form filled and Proceed to Checkout clicked",
                    "Exception: " + e.getMessage(), "FAIL");
        }
    }

    @Test(priority = 6)
    public void tc006_CaptureValidationError() {
        try {
            String  error    = giftPage.getFormErrorMessage();
            String  expected = "Receiver's Email ID Cannot Be Empty";
            boolean pass     = expected.equalsIgnoreCase(error.trim());
            log("TC_006", "Capture Validation Error",
                    "Read error shown when receiver email is missing",
                    expected,
                    error.isEmpty() ? "No error shown" : "Error: " + error,
                    pass ? "PASS" : "FAIL");
        } catch (Exception e) {
            log("TC_006", "Capture Validation Error",
                    "Read error shown when receiver email is missing",
                    "Receiver's Email ID Cannot Be Empty",
                    "Exception: " + e.getMessage(), "FAIL");
        }
    }

    @Test(priority = 7)
    public void tc007_VerifyPriceBelow500() {
        try {
            List<String[]> products = resultsPage.getAllProductNamesAndPrices(1);

            if (products.isEmpty()) {
                log("TC_007", "Verify Product Price < Rs.500",
                        "Check if first product price is below Rs.500",
                        "Price < Rs.500",
                        "No products found", "FAIL");
                return;
            }

            String  name  = products.get(0)[0];
            int     price = Integer.parseInt(products.get(0)[1].replaceAll("[^0-9]", ""));
            boolean pass  = price < 500;
            log("TC_007", "Verify Product Price < Rs.500",
                    "Check if '" + name + "' price is below Rs.500",
                    "Price < Rs.500",
                    "Actual Price: Rs." + price,
                    pass ? "PASS" : "FAIL");

        } catch (Exception e) {
            log("TC_007", "Verify Product Price < Rs.500",
                    "Check if first product price is below Rs.500",
                    "Price < Rs.500",
                    "Exception: " + e.getMessage(), "FAIL");
        }
    }

    private void log(String id, String name, String desc,
                    String expected, String actual, String status) {
        reporter.logResult(serial++, id, name, desc, expected, actual, status);
    }
}
