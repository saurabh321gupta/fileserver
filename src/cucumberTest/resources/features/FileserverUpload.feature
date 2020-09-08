Feature: Fileserver upload

  Scenario: Upload new image with success
    Given browser "Google Chrome"
    When I open "localhost:4200"
    And I set "file  description" value to "File description placeholder"
    And I click on "Choose File button"
    And I click on "image.jpeg"
    And I press on "ENTER"
    And I click on "Submit image button"
    Then I should see text "Success" in "Response message placeholder"

  Scenario: Upload new image without success due to missing description
    Given browser "Google Chrome"
    When I open "localhost:4200"
    And I click on "Choose File button"
    And I click on "image.jpeg"
    And I press on "ENTER"
    And I click on "Submit image button"
    Then I should see text "Failed" in "Response message placeholder"
    And I should see text "Required" in "File description error placeholder"