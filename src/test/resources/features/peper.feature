Feature: Pepperfry End-to-End Tests

  Scenario: Verify Home Decor menu items are displayed on hover
    Given the user launches the Pepperfry website
    And any popup on the page is closed
    When the user hovers over the Home Decor menu
    Then the Home Decor menu items should be captured and printed

  Scenario: Search for Bookshelves and filter by price and brand
    Given the user opens the Pepperfry website
    And the user closes the popup if present
    When the user searches for "Bookshelves"
    And the user applies a maximum price filter of 15000 and selects the brand "WoodenMood"
    And the top 3 products with their prices should be displayed
    Then the user validates that prices are less than 15000

  Scenario: Validate sender email while purchasing a gift card using properties file
    Given the user navigates to the Pepperfry home page
    And any visible popup on the home page is closed
    When the user opens gift card option
    And the user selects the birthday gift card
    And the user enters gift card details from the properties file
    And the user selects the 1000 denomination
    And the user clicks proceed to checkout
    Then the sender email validation message should be displayed correctly
