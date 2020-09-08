Feature: Fileserver search

  Scenario: Initial page load
    Given browser "Google Chrome"
    When I open "localhost:4200"
    Then existing records should be displayed
    And the application shows up to 20 records

  Scenario: Search image with success
    Given browser "Google Chrome"
    When I open "localhost:4200"
    And I set "file description" value to "File description filter placeholder"
    And I click on "Submit search"
    Then "search-results" should be displayed
    And "search-results" has 1 result
    And I should see text "file description" in "search-results 1st row file description placeholder"
    And I should see the image description, image size, and image file type