Feature: Employee Management

  As an HR administrator
  I want to manage employee records
  So that the organization can keep up‑to‑date personnel information

  Background:
    Given the user is logged into the HR system
    And the user navigates to the "viewEmployeeList" page

  # ------------------------------------------------------------------
  # 1. Add multiple employees (Scenario Outline)
  # ------------------------------------------------------------------

  Scenario Outline: Add multiple employees
    When the user clicks the "Add Employee" button
    And the user enters first name "<FirstName>"
    And the user enters middle name "<MiddleName>"
    And the user enters last name "<LastName>"
    And the user clicks the Save button
    Then the employee should be created successfully

    Examples:
      | FirstName | MiddleName | LastName |
      | Virat     | rcb          | Kohli     |

  # ------------------------------------------------------------------
  # 2. Search employee
  # ------------------------------------------------------------------
  Scenario: Search employee by ID
    When the user enters employee ID "Emily" into the search field
    And the user clicks the Search button
    Then the employee details page for "09557" should be displayed
    And the employee name should be "Emily Jones"

  # ------------------------------------------------------------------
  # 3. Update employee details
  # ------------------------------------------------------------------
  Scenario: Update employee email
    When the user navigates to the employee details page for "<EmployeeID>"
    And the user clicks the "Edit" button
    And the user updates the email to "<NewEmail>"
    And the user clicks the "Save" button
    Then the employee details page should show the updated email "<NewEmail>"

  # ------------------------------------------------------------------
  # 4. Delete employee
  # ------------------------------------------------------------------
  Scenario: Delete employee
    When the user navigates to the employee details page for "<EmployeeID>"
    And the user clicks the "Delete" button
    And the user confirms the deletion
    Then the employee "<EmployeeID>" should no longer exist in the system
    And a confirmation message "Employee deleted successfully" should be displayed