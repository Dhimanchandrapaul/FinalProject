package tests;

import config.TestConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.GiftCardPage;
import pages.HomePage;
import pages.SearchResultsPage;
import utils.ExcelReporter;
import java.time.Duration;
import java.util.List;

public class PepperfryTest {

    static WebDriver         driver;
    static HomePage          homePage;
    static SearchResultsPage resultsPage;
    static GiftCardPage      giftPage;
    static ExcelReporter     reporter;        // ← no longer initialized here
    static int               serial = 1;

    public static void main(String[] args) {

        reporter = new ExcelReporter(         // ← moved here
                "C:/Users/2479652/IdeaProjects/FinalProject/Resources/PepperfryTestResults.xlsx");


        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60)); // ← added
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));  // ← added
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));   // ← added

        homePage    = new HomePage(driver);
        resultsPage = new SearchResultsPage(driver);
        giftPage    = new GiftCardPage(driver);

        try {
            tc001_OpenWebsite();
            tc002_SearchBookshelves();
            tc003_ApplyPriceAndBrandFilter();
            tc004_VerifyAllProductPrices();
            tc005_FillGiftCardForm();
            tc006_CaptureValidationError();
            tc007_VerifyPriceBelow500();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        } finally {
            reporter.save();   // ← save FIRST
            driver.quit();     // ← then close
        }
    }

    static void tc001_OpenWebsite() {
        try {
            homePage.open(TestConfig.WEBSITE_URL);
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

    static void tc002_SearchBookshelves() {
        try {
            homePage.searchFor(TestConfig.SEARCH_KEYWORD);
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

    static void tc003_ApplyPriceAndBrandFilter() {
        try {
            resultsPage.openMoreFilters();
            resultsPage.expandFilter(TestConfig.PRICE_FILTER);
            resultsPage.setMaxPrice(TestConfig.MAX_PRICE);
            resultsPage.expandFilter(TestConfig.BRAND_FILTER);
            resultsPage.selectBrand(TestConfig.BRAND_NAME);
            resultsPage.clickApply(TestConfig.APPLY_BUTTON);
            String  url      = driver.getCurrentUrl();
            boolean hasPrice = url.contains(String.valueOf(TestConfig.MAX_PRICE));
            boolean hasBrand = url.toLowerCase().contains(TestConfig.BRAND_NAME.toLowerCase());
            boolean pass     = hasPrice && hasBrand;
            log("TC_003", "Apply Price + Brand Filter",
                    "Set max price Rs." + TestConfig.MAX_PRICE +
                            " → Select brand '" + TestConfig.BRAND_NAME + "' → Click APPLY",
                    "URL contains Rs." + TestConfig.MAX_PRICE +
                            " and '" + TestConfig.BRAND_NAME + "'",
                    "URL: " + url,
                    pass ? "PASS" : "FAIL");
        } catch (Exception e) {
            log("TC_003", "Apply Price + Brand Filter",
                    "Set max price Rs." + TestConfig.MAX_PRICE +
                            " → Select brand '" + TestConfig.BRAND_NAME + "' → Click APPLY",
                    "URL contains Rs." + TestConfig.MAX_PRICE +
                            " and '" + TestConfig.BRAND_NAME + "'",
                    "Exception: " + e.getMessage(), "FAIL");
        }
    }

    // ── TC_004 / TC_005 / TC_006 : Verify Each Product Price < Rs.15000 ──────────
    static void tc004_VerifyAllProductPrices() {
        try {
            List<String[]> products = resultsPage.getAllProductNamesAndPrices(TestConfig.TOP_N);

            if (products.isEmpty()) {
                log("TC_004", "Verify Product Price < Rs.15000",
                        "Check each product price is below Rs.15000",
                        "At least 1 product below Rs." + TestConfig.MAX_PRICE,
                        "No products found on page", "FAIL");
                return;
            }

            for (int i = 0; i < products.size(); i++) {
                String  tcId     = String.format("TC_%03d", serial);  // TC_004, TC_005, TC_006
                String  name     = products.get(i)[0];
                String  priceStr = products.get(i)[1].replaceAll("[^0-9]", "");
                int     price    = Integer.parseInt(priceStr);
                boolean pass     = price < TestConfig.MAX_PRICE;

                log(tcId, "Verify Product Price < Rs.15000",
                        "Check price of: " + name,
                        "Price < Rs." + TestConfig.MAX_PRICE,
                        "Actual Price: Rs." + price,
                        pass ? "PASS" : "FAIL");
            }

        } catch (Exception e) {
            log("TC_004", "Verify Product Price < Rs.15000",
                    "Check each product price is below Rs.15000",
                    "All prices < Rs." + TestConfig.MAX_PRICE,
                    "Exception: " + e.getMessage(), "FAIL");
        }
    }

    static void tc005_FillGiftCardForm() {
        try {
            homePage.goToGiftCards();
            giftPage.selectBirthdayCard();
            giftPage.fillGiftCardForm(
                    TestConfig.GC_RECIPIENT_NAME, TestConfig.GC_SENDER_NAME,
                    TestConfig.GC_RECIPIENT_MOBILE, TestConfig.GC_SENDER_MOBILE,
                    TestConfig.GC_SENDER_EMAIL, TestConfig.GC_MESSAGE);
            giftPage.selectAmount1000();
            giftPage.clickProceedToCheckout();
            log("TC_005", "Fill Gift Card Form",
                    "Go to Gift Cards → Birthday card → fill form → Rs.1000 → Proceed",
                    "Form filled and Proceed to Checkout clicked",
                    "Recipient: " + TestConfig.GC_RECIPIENT_NAME +
                            " | Sender: " + TestConfig.GC_SENDER_NAME +
                            " | Rs.1000 selected | Proceed clicked", "PASS");
        } catch (Exception e) {
            log("TC_005", "Fill Gift Card Form",
                    "Go to Gift Cards → Birthday card → fill form → Rs.1000 → Proceed",
                    "Form filled and Proceed to Checkout clicked",
                    "Exception: " + e.getMessage(), "FAIL");
        }
    }

    static void tc006_CaptureValidationError() {
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

    // ── TC_007 : Verify Product Price < Rs.500 (Intentional FAIL) ────────────────
    static void tc007_VerifyPriceBelow500() {
        try {
            List<String[]> products = resultsPage.getAllProductNamesAndPrices(1);

            if (products.isEmpty()) {
                log("TC_007", "Verify Product Price < Rs.500",
                        "Check if first product price is below Rs.500",
                        "Price < Rs.500",
                        "No products found", "FAIL");
                return;
            }

            String  name     = products.get(0)[0];
            String  priceStr = products.get(0)[1].replaceAll("[^0-9]", "");
            int     price    = Integer.parseInt(priceStr);
            boolean pass     = price < 500;   // ← will always FAIL, bookshelves cost more

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

    static void log(String id, String name, String desc,
                    String expected, String actual, String status) {
        reporter.logResult(serial++, id, name, desc, expected, actual, status);
    }
}