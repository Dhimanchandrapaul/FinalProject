Feature: Pepperfry End-to-End Tests

  Scenario: Open Pepperfry website and close popup
    Given I open the Pepperfry website
    When I close the popup if present
    Then the page title should contain "Pepperfry"

  Scenario: Search for Bookshelves
    Given I am on the Pepperfry homepage
    When I search for "Bookshelves"
    Then the URL should contain "search" or "bookshelves"

  Scenario: Apply Price and Brand Filter
    Given I am on the search results page
    When I apply a maximum price of 15000 and select brand "WoodenMood"
    Then the URL should contain "15000" and "WoodenMood"

  Scenario: Verify all product prices are below 15000
    Given I am on the filtered search results page
    Then each product price should be below 15000

  Scenario: Fill Gift Card Form
    Given I am on the Pepperfry homepage
    When I go to Gift Cards and select Birthday card
    And I fill the gift card form with valid details except receiver email
    And I select Rs.1000 and proceed to checkout
    Then the form should be submitted

  Scenario: Capture validation error for missing receiver email
    Given I am on the gift card checkout page with missing receiver email
    Then I should see the error message "Receiver's Email ID Cannot Be Empty"

  Scenario: Verify first product price is below 15000
    Given I am on the filtered search results page
    Then the first product price should be below 15000