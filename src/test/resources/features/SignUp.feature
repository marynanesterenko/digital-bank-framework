Feature: sign up functionality

  Scenario: verify user is able to register
    Given user is on the login page
    When user user clicks on sign up here button
    And user fills out all the fields on the first form
    And user fills out all the fields on the second form
    Then user account is created
    And user record is created in the database