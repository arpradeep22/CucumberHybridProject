Feature: Orange HRM Login Functionality
  As a user
  I want to log in to the Orange HRM system
  So that I can access the HR management features

  Background:
    Given the user is on the Orange HRM login page


  Scenario: Login with valid credentials
    When the user enters "Admin" in the username field
    And the user enters "admin123" in the password field
    And the user clicks the Login button
    Then the user should be redirected to the dashboard
    And the user should see the welcome message


  Scenario: Login with invalid username
    When the user enters "InvalidUser" in the username field
    And the user enters "admin123" in the password field
    And the user clicks the Login button
    Then an error message should appear
    And the error message should display "Invalid credentials"
    And the user should remain on the login page

  Scenario: Login with invalid password
    When the user enters "Admin" in the username field
    And the user enters "wrongpassword" in the password field
    And the user clicks the Login button
    Then an error message should appear
    And the error message should display "Invalid credentials"
    And the user should remain on the login page

  Scenario: Login with empty fields
    When the user clicks the Login button without entering credentials
    Then validation error messages should appear
    And the username field should display a required error
    And the password field should display a required error
    And the user should remain on the login page

  Scenario: Successful logout
    Given the user is logged in with valid credentials
    When the user clicks on the user profile dropdown
    And the user clicks the Logout button
    Then the user should be redirected to the login page
    And the session should be terminated