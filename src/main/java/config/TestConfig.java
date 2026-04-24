package config;

public class TestConfig {

    private TestConfig() {} // Utility class – no instantiation

    // ── Site ────────────────────────────────────────────────────────────────────
    public static final String WEBSITE_URL        = "https://www.pepperfry.com/";

    // ── Search & Filters ────────────────────────────────────────────────────────
    public static final String SEARCH_KEYWORD     = "Bookshelves";
    public static final String PRICE_FILTER       = "Price";
    public static final int    MAX_PRICE          = 15000;
    public static final String BRAND_FILTER       = "Brand";
    public static final String BRAND_NAME         = "WoodenMood";
    public static final String APPLY_BUTTON       = "APPLY";
    public static final int    TOP_N              = 3;
    public static final int    GC_AMOUNT          = 1000;

    // ── Gift Card Form Data ──────────────────────────────────────────────────────
    public static final String GC_RECIPIENT_NAME   = "nishank";
    public static final String GC_SENDER_NAME      = "Dhiman";
    public static final String GC_RECIPIENT_MOBILE = "9999999999";
    public static final String GC_SENDER_MOBILE    = "5555555555";
    public static final String GC_SENDER_EMAIL     = "Dhiman@gmail.com";
    public static final String GC_MESSAGE          = "Happy Birthday! Enjoy shopping";
}