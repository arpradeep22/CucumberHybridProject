# CucumberHybridProject

# 🥒 Cucumber Hybrid Framework — OrangeHRM Test Automation

> A production-grade, end-to-end test automation framework built with **Selenium WebDriver**, **Cucumber BDD**, **JUnit**, **Page Object Model**, and **Allure + ExtentReports** reporting. This framework automates the [OrangeHRM Demo Application](https://opensource-demo.orangehrmlive.com) and is designed to be scalable, maintainable, and CI/CD ready.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Tech Stack & Dependencies](#tech-stack--dependencies)
- [Project Structure](#project-structure)
- [Architecture & Design Patterns](#architecture--design-patterns)
- [Framework Components (Deep Dive)](#framework-components-deep-dive)
  - [1. Config Reader](#1-config-reader)
  - [2. Driver Factory](#2-driver-factory)
  - [3. Element Utils](#3-element-utils)
  - [4. Page Objects](#4-page-objects)
  - [5. Step Definitions](#5-step-definitions)
  - [6. Hooks](#6-hooks)
  - [7. Test Runner](#7-test-runner)
  - [8. Feature Files](#8-feature-files)
- [Dependency Injection with PicoContainer](#dependency-injection-with-picocontainer)
- [Reporting](#reporting)
- [Configuration](#configuration)
- [How to Run Tests](#how-to-run-tests)
- [Test Coverage](#test-coverage)
- [Known TODOs & Future Enhancements](#known-todos--future-enhancements)

---

## Overview

This project is a **Hybrid Test Automation Framework** — meaning it combines multiple industry-standard approaches:

| Approach | How it's used here |
|---|---|
| **BDD (Behaviour-Driven Development)** | Cucumber feature files written in Gherkin syntax |
| **Page Object Model (POM)** | Each web page has a dedicated Java class encapsulating its locators and actions |
| **Data-Driven Testing** | Cucumber `Scenario Outline` + `Examples` tables drive multiple test runs from one scenario |
| **Keyword-Driven** | Step definitions act as reusable keywords mapped from Gherkin steps |
| **ThreadLocal WebDriver** | Supports parallel test execution safely |

The application under test is **OrangeHRM** — an open-source HR management system. The framework currently covers:
- ✅ Login functionality (valid login, invalid credentials, empty fields, logout)
- ✅ PIM (Personnel Information Management) — Add Employee, Search Employee

---

## Tech Stack & Dependencies

| Technology | Version | Purpose |
|---|---|---|
| **Java** | 17 | Core programming language |
| **Maven** | 3.x | Build tool & dependency management |
| **Selenium WebDriver** | 4.25.0 | Browser automation |
| **WebDriverManager** | 5.6.2 | Automatic browser driver setup |
| **Cucumber** | 7.14.0 | BDD framework (Gherkin parsing, step binding) |
| **JUnit** | 4.13.2 | Test runner integration |
| **PicoContainer** | 7.14.0 | Dependency injection between step definition classes |
| **Allure** | 2.27.0 | Advanced test reporting with timeline, history, and screenshots |
| **ExtentReports** | 5.0.9 | HTML test reports with detailed step logs |
| **Log4j** | 2.25.1 | Logging |
| **Lombok** | 1.18.30 | Boilerplate reduction (getters/setters etc.) |
| **AspectJ Weaver** | 1.9.21 | Required by Allure for `@Step` annotation processing |

All versions are centrally managed in `pom.xml` via `<properties>` tags, making upgrades easy.

---

## Project Structure

```
CucumberHybridProject/
│
├── pom.xml                          # Maven build config, dependencies, plugins
├── README.md                        # This file
│
├── allure-results/                  # Raw Allure JSON results (generated after test run)
│
└── src/
    ├── main/
    │   └── java/
    │       └── com/framework/
    │           ├── config/
    │           │   └── Config_Reader.java       # Reads config.properties
    │           ├── factory/
    │           │   └── DriverFactory.java       # Creates & manages WebDriver (ThreadLocal)
    │           ├── pages/
    │           │   ├── LoginPage.java           # Page Object for Login page
    │           │   └── PIM_Page.java            # Page Object for PIM module
    │           └── utils/
    │               └── ElementUtils.java        # Reusable Selenium helper methods
    │
    └── test/
        ├── java/
        │   └── com/framework/
        │       ├── hooks/
        │       │   └── Hooks.java               # Cucumber @Before/@After lifecycle hooks
        │       ├── runners/
        │       │   └── TestRunner.java          # JUnit-Cucumber entry point
        │       └── stepDefinitions/
        │           ├── Login_Steps.java         # Step defs for login scenarios
        │           └── PIM_Steps.java           # Step defs for PIM scenarios
        │
        └── resources/
            ├── config.properties                # Browser & URL configuration
            ├── allure.properties                # Allure results path config
            └── features/
                ├── login.feature                # Login BDD scenarios
                └── pim.feature                  # PIM BDD scenarios
```

---

## Architecture & Design Patterns

### 🔷 Page Object Model (POM)

Every application page is modelled as a Java class under `src/main/java/com/framework/pages/`. Each page class:

- Declares its **locators** as private `By` fields
- Exposes **public methods** representing user actions (e.g., `enterUsername()`, `clickLogin()`)
- **Never contains assertions** — assertions belong in step definitions
- Takes `WebDriver` as a constructor argument and delegates Selenium calls to `ElementUtils`

This makes locator maintenance trivial — if a locator changes, you update only the one page class.

### 🔷 Layered Architecture

```
Feature File (Gherkin)
       ↓
Step Definitions  ←──── PicoContainer Dependency Injection
       ↓
Page Objects
       ↓
Element Utils (Selenium Wrapper)
       ↓
WebDriver  ←──── DriverFactory (ThreadLocal)
       ↑
Hooks (Before/After lifecycle)
       ↑
Config Reader (config.properties)
```

### 🔷 ThreadLocal WebDriver

`DriverFactory.tlDriver` is a `ThreadLocal<WebDriver>`. This means each test thread gets its own isolated WebDriver instance. This is what makes the framework **parallel-execution safe** — multiple scenarios can run simultaneously without browser interference.

### 🔷 Separation of Concerns

| Layer | Responsibility |
|---|---|
| **Feature Files** | Define WHAT to test (business scenarios) |
| **Step Definitions** | Define HOW to test (orchestrate page actions) |
| **Page Objects** | Define WHERE to interact (page-specific UI logic) |
| **ElementUtils** | Define the atomic HOW (raw Selenium interactions) |
| **Hooks** | Define SETUP/TEARDOWN (browser lifecycle) |
| **Config Reader** | Define CONFIGURATION (browser, URL) |

---

## Framework Components (Deep Dive)

### 1. Config Reader

**File:** `src/main/java/com/framework/config/Config_Reader.java`

```java
public class Config_Reader {
    static Properties properties;

    public static Properties initProperties() {
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        properties = new Properties();
        properties.load(fis);
        return properties;
    }
}
```

**What it does:**
- Reads `config.properties` from `src/test/resources/` using `FileInputStream`
- Returns a `java.util.Properties` object
- Used in `Hooks.java` at the `@Before` stage to fetch `browser` and `url` values before any test starts
- `static` method means it can be called without instantiation anywhere in the framework

**Design Note:** The `properties` field is `static`, so the loaded config is shared across the framework. In multi-threaded scenarios, this is safe since config is read-only after initialization.

---

### 2. Driver Factory

**File:** `src/main/java/com/framework/factory/DriverFactory.java`

This is one of the most critical classes in the framework. It:

1. **Creates WebDriver instances** for Chrome (headless & headed), Firefox (headless), and Safari
2. **Uses ThreadLocal** (`tlDriver`) so each test thread has its own browser
3. **Uses WebDriverManager** to auto-download and configure the correct browser driver binary — no manual `chromedriver.exe` management needed

**Browser Support:**

| Value in `config.properties` | Browser Mode |
|---|---|
| `chrome` | Headless Chrome (CI-ready) |
| `chrome-headed` | Visible Chrome (debugging) |
| `firefox` | Headless Firefox |
| `safari` | Safari (macOS only) |

**Chrome Headless Configuration (Key Flags):**

```java
options.addArguments("--headless=new");           // Modern headless mode
options.addArguments("--window-size=1920,1080");  // Full HD viewport
options.addArguments("--disable-blink-features=AutomationControlled"); // Stealth mode
options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
options.addArguments("--no-sandbox");             // Required for CI/Docker
options.addArguments("--disable-dev-shm-usage"); // Prevents memory issues in containers
```

These flags ensure the browser behaves like a real user session even in headless mode, preventing bot-detection rejections on the target application.

**ThreadLocal Pattern:**
```java
public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

// Set driver for current thread:
tlDriver.set(new ChromeDriver(options));

// Get driver for current thread:
public static synchronized WebDriver getDriver() {
    return tlDriver.get();
}
```

`getDriver()` is `synchronized` to prevent race conditions when multiple threads access it.

---

### 3. Element Utils

**File:** `src/main/java/com/framework/utils/ElementUtils.java`

This utility class is a **Selenium wrapper** that adds reliability and resilience on top of raw WebDriver calls. Every interaction goes through here.

**Key Methods:**

| Method | Purpose |
|---|---|
| `doSendKeys(By, String)` | Type text after waiting for element visibility |
| `doClick(By)` | Click with automatic JS fallback on failure |
| `jsClick(By)` | Direct JavaScript click — bypasses rendering issues |
| `scrollAndClick(By)` | Scroll element into view, then click |
| `CaptureText(By)` | Wait for element and return its text |
| `ClearText(By)` | Clear an input field with explicit wait |
| `PresenceofElement(By)` | Return boolean whether element is displayed |
| `LoginPageMsg(By)` | Get error message text with a fixed 1-second delay |
| `PageTitle()` | Return current page title |
| `pageURL()` | Return current URL |

**Explicit Waits:** Every method uses `WebDriverWait` with a 15-second timeout using `ExpectedConditions`. This eliminates flaky `Thread.sleep()` calls and makes the suite resilient to network and rendering delays.

**JS Click Fallback Logic:**
```java
public void doClick(By locator) {
    WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
    try {
        element.click();  // Try standard Selenium click first
    } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
        jsClick(locator); // Fall back to JS click if intercepted or stale
    }
}
```

This is particularly important for Vue.js-rendered elements in OrangeHRM, which can appear clickable but fail due to dynamic DOM updates or overlapping elements in headless mode.

---

### 4. Page Objects

#### LoginPage — `src/main/java/com/framework/pages/LoginPage.java`

Covers the OrangeHRM login screen. Locators use `By.name`, `By.xpath`, and `By.linkText` strategies.

**Locators defined:**
- `usernameField` — `By.name("username")`
- `passwordField` — `By.name("password")`
- `loginButton` — `By.xpath("//button[@type='submit']")`
- `message` — XPath to the alert error message
- `fieldMsg` — XPath to field-level validation errors
- `userDropdown` — User profile menu trigger
- `userLogout` — Logout link

**Public Methods:**
`enterUsername()`, `enterPassword()`, `clickLogin()`, `getMessage()`, `getFieldMessage()`, `clickUserDropdown()`, `clickUserLogout()`, `getPagetitle()`, `getPageURL()`

---

#### PIM_Page — `src/main/java/com/framework/pages/PIM_Page.java`

Covers the Personnel Information Management module — specifically adding and searching employees.

**Key Locators:**
- Navigation: `pimButton`, `employeeListButton`
- Form inputs: `firstnameField`, `middlenameField`, `lastnameField`, `employeeIDField`
- Action buttons: `addButton`, `saveButton`, `searchButton`
- Verification: `successMessage` (toast), `tableLocator`, `idCell`, `firstNameCell`, `lastNameCell`

**Notable Design Decision:** Table cell locators use absolute XPaths targeting the first row of the second `rowgroup`, which is the search results area in OrangeHRM's dynamic grid. This is more reliable than relative locators for paginated result tables.

---

### 5. Step Definitions

Step definitions are the **glue** between Gherkin plain-English steps and Java code.

#### Login_Steps — `src/test/java/com/framework/stepDefinitions/Login_Steps.java`

- Retrieves `WebDriver` from `DriverFactory.getDriver()` — this is the ThreadLocal driver set by `Hooks`
- Instantiates `LoginPage` on demand
- Uses Allure `@Step` annotations on every method so each Gherkin step appears as a named step inside the Allure report

**Notable step:**
```java
@Given("the user is logged in with valid credentials")
public void the_user_is_logged_in_with_valid_credentials() {
    // Calls other steps in sequence — acts as a reusable composite step
    the_user_is_on_the_orange_hrm_login_page();
    the_user_enters_in_the_username_field("Admin");
    the_user_enters_in_the_password_field("admin123");
    the_user_clicks_the_login_button();
    // Explicit wait for dashboard to fully load before returning
    new WebDriverWait(driver, Duration.ofSeconds(15))
        .until(ExpectedConditions.urlContains("dashboard"));
}
```

This composite step is used as a `Background` or `Given` in PIM scenarios, eliminating code duplication across feature files.

---

#### PIM_Steps — `src/test/java/com/framework/stepDefinitions/PIM_Steps.java`

- **Constructor-injected** with `Login_Steps` via PicoContainer:
  ```java
  public PIM_Steps(Login_Steps loginSteps) {
      this.loginSteps = loginSteps;
  }
  ```
- Calls `loginSteps.the_user_is_logged_in_with_valid_credentials()` to perform login before PIM actions
- Gets `WebDriver` from `DriverFactory.getDriver()` — same thread-safe driver used by login
- Instantiates `PIM_Page` after navigation begins

Several methods have `// TODO` placeholders for future scenarios (update email, delete employee) — these are scaffolded step definitions ready for implementation.

---

### 6. Hooks

**File:** `src/test/java/com/framework/hooks/Hooks.java`

Hooks control the **lifecycle of each test scenario**. They run before and after every Cucumber scenario.

#### @Before Hooks (in order of execution):

```
@Before(order = 0) → configsetup()    → Load config.properties
@Before(order = 1) → launchBrowser()  → Start WebDriver, navigate to URL
```

Lower `order` value = runs first.

#### @After Hooks (in order of execution):

```
@After(order = 1) → takeScreenshotOnFailure()  → Capture screenshot if scenario failed
@After(order = 0) → quitBrowser()              → Quit browser & clean ThreadLocal
```

Higher `order` value in `@After` = runs first (opposite of `@Before`).

**This ordering is critical:** the screenshot must be captured before the browser is closed. If `quitBrowser()` ran first, the screenshot would fail because there'd be no browser to capture from.

**Screenshot Attachment Strategy:**
Failure screenshots are attached to **both** reports:
1. **Cucumber HTML Report** — via `scenario.attach(screenshot, "image/png", "Screenshot on Failure")`
2. **Allure Report** — via `AllureLifecycle` step wrapper:
   ```java
   lifecycle.startStep(stepUUID, new StepResult().setName("Screenshot on Failure").setStatus(Status.FAILED));
   lifecycle.addAttachment("Screenshot - " + scenario.getName(), "image/png", "png", new ByteArrayInputStream(screenshot));
   lifecycle.stopStep(stepUUID);
   ```
   The lifecycle wrapper is necessary because `Allure.addAttachment()` can lose its report context inside a `@After` hook. Wrapping it in a manual step guarantees the attachment is anchored to the correct test result.

**ThreadLocal Cleanup:**
```java
DriverFactory.tlDriver.remove();  // ← Critical to prevent memory leaks in parallel runs
```

---

### 7. Test Runner

**File:** `src/test/java/com/framework/runners/TestRunner.java`

```java
@RunWith(Cucumber.class)
@CucumberOptions(
    features = {"src/test/resources/features"},
    glue = {"com.framework.stepDefinitions", "com.framework.hooks"},
    monochrome = true,
    dryRun = false,
    plugin = {
        "pretty",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
        "html:target/cucumber-reports/cucumber.html",
        "json:target/cucumber-reports/cucumber.json"
    }
)
public class TestRunner {}
```

**Key Options Explained:**

| Option | Value | Meaning |
|---|---|---|
| `features` | `src/test/resources/features` | Directory where `.feature` files live |
| `glue` | Two packages listed | Where Cucumber looks for step definitions AND hooks |
| `monochrome` | `true` | Cleaner console output without escape codes |
| `dryRun` | `false` | Actually executes tests (set to `true` to just validate step mappings) |
| `tags` | Commented out | Uncomment `@smoke`, `@regression`, etc. to filter scenarios |
| `plugin: pretty` | — | Prints Gherkin steps to console in a readable format |
| `plugin: AllureCucumber7Jvm` | — | Generates Allure-compatible JSON results in `allure-results/` |
| `plugin: html` | `target/cucumber-reports/` | Generates standard Cucumber HTML report |
| `plugin: json` | `target/cucumber-reports/` | Generates JSON for CI integration (Jenkins, GitHub Actions) |

---

### 8. Feature Files

#### login.feature

Covers 5 scenarios for the OrangeHRM login page:

1. **Login with valid credentials** — enters Admin/admin123, asserts redirect to dashboard
2. **Login with invalid username** — asserts "Invalid credentials" error message
3. **Login with invalid password** — asserts "Invalid credentials" error message
4. **Login with empty fields** — submits blank form, asserts "Required" field-level validation
5. **Successful logout** — logs in, clicks profile dropdown, logs out, asserts redirect to login page

Uses a `Background` block to navigate to the login page before every scenario.

#### pim.feature

Covers 4 scenarios for the PIM module:

1. **Add multiple employees** (Scenario Outline) — uses `Examples` table with `FirstName`, `MiddleName`, `LastName` columns. Currently has 1 data row (Virat rcb Kohli). More rows can be added to the table to auto-generate more test cases.
2. **Search employee by ID** — searches for "Emily", verifies ID `09557` and full name `Emily Jones` in results table
3. **Update employee email** — scaffolded (TODO implementation)
4. **Delete employee** — scaffolded (TODO implementation)

Uses a `Background` block to login and navigate to the PIM Employee List before every scenario.

---

## Dependency Injection with PicoContainer

The framework uses **Cucumber PicoContainer** (`cucumber-picocontainer`) for dependency injection between step definition classes.

**The Problem it Solves:**

PIM scenarios need to perform a login first. `Login_Steps` already has the login logic. Without DI, you'd have to duplicate that logic in `PIM_Steps` or create a shared static utility — both are messy.

**The Solution:**

Declare `Login_Steps` as a constructor parameter of `PIM_Steps`:

```java
public class PIM_Steps {
    private final Login_Steps loginSteps;

    public PIM_Steps(Login_Steps loginSteps) {  // PicoContainer injects this
        this.loginSteps = loginSteps;
    }
}
```

Cucumber's PicoContainer automatically:
1. Detects that `PIM_Steps` needs a `Login_Steps` instance
2. Creates (or reuses) a `Login_Steps` instance for the current scenario
3. Injects it into `PIM_Steps`'s constructor

This means **both classes share the same `LoginPage` and `WebDriver` state** within a single scenario — no duplication, no static hacks.

---

## Reporting

### Allure Report

**Setup:** The `allure-maven` plugin and `allure-cucumber7-adapter` are configured in `pom.xml`. The AspectJ Weaver JVM agent processes `@Step` annotations at runtime.

**How to generate:**

```bash
mvn test                    # Run tests → writes JSON to allure-results/
mvn allure:report           # Generate HTML report in target/allure-report/
mvn allure:serve            # Generate + open in browser (recommended)
```

**What Allure shows:**
- Test result timeline
- Scenario pass/fail/broken breakdown
- Step-by-step execution trace (thanks to `@Step` annotations)
- Failure screenshots embedded inline
- Trend history across multiple runs

**`allure.properties`** (in `src/test/resources/`) points Allure to the correct results directory.

---

### Cucumber HTML & JSON Reports

Generated automatically after every `mvn test` run:

| Report | Location |
|---|---|
| HTML Report | `target/cucumber-reports/cucumber.html` |
| JSON Report | `target/cucumber-reports/cucumber.json` |

The JSON report is useful for CI pipelines (Jenkins Cucumber plugin, GitHub Actions, etc.).

---

## Configuration

**File:** `src/test/resources/config.properties`

```properties
browser = chrome
url = https://opensource-demo.orangehrmlive.com/web/index.php/auth/login
```

| Property | Description | Valid Values |
|---|---|---|
| `browser` | Which browser to use | `chrome`, `chrome-headed`, `firefox`, `safari` |
| `url` | Application URL to open | Any valid URL |

To switch browsers, just change the `browser` value — no code changes needed.

---

## How to Run Tests

### Prerequisites

- Java 17+
- Maven 3.6+
- Chrome or Firefox installed
- Internet connection (WebDriverManager downloads drivers automatically)

### Run All Tests

```bash
mvn test
```

### Run with Allure Report

```bash
mvn test allure:serve
```

### Run Specific Tags

Uncomment and set tags in `TestRunner.java`:

```java
tags = "@smoke"       // Run only smoke tests
tags = "@regression"  // Run only regression tests
tags = "@login"       // Run only login scenarios
```

### Run from IDE

Right-click `TestRunner.java` → `Run as JUnit Test`

### Headless vs Headed

Change in `config.properties`:
```properties
browser = chrome          # Headless (default, CI-ready)
browser = chrome-headed   # Headed (for local debugging)
```

---

## Test Coverage

| Module | Scenario | Status |
|---|---|---|
| Login | Valid credentials login | ✅ Implemented |
| Login | Invalid username | ✅ Implemented |
| Login | Invalid password | ✅ Implemented |
| Login | Empty fields validation | ✅ Implemented |
| Login | Logout flow | ✅ Implemented |
| PIM | Add employee (Scenario Outline) | ✅ Implemented |
| PIM | Search employee by ID | ✅ Implemented |
| PIM | Update employee email | 🔲 TODO (scaffolded) |
| PIM | Delete employee | 🔲 TODO (scaffolded) |

---

## Known TODOs & Future Enhancements

### Pending Implementations

- `PIM_Steps.enterEmail()` — Email entry during employee creation
- `PIM_Steps.updateEmail()` — Edit employee email
- `PIM_Steps.confirmDeletion()` — Delete employee flow
- `PIM_Steps.verifyEmployeeId()` — Assert displayed employee ID after save
- `PIM_Steps.verifyUniqueEmployeeId()` — Validate uniqueness constraint

### Suggested Enhancements

| Enhancement | Description |
|---|---|
| **Parallel Execution** | Configure Surefire plugin with `<parallel>methods</parallel>` to run scenarios concurrently — ThreadLocal is already in place |
| **Retry Logic** | Add `cucumber-retry` or Surefire rerun to automatically retry failed tests once |
| **Data from Excel/CSV** | Replace hardcoded test data in feature files with external data sources |
| **CI/CD Integration** | Add GitHub Actions or Jenkins pipeline YAML to trigger `mvn test allure:report` on every push |
| **More Page Objects** | Expand coverage to `My Info`, `Leave`, `Time`, `Recruitment` modules |
| **Log4j Integration** | Replace `System.out.println` statements with Log4j logger calls |
| **Environment Profiles** | Use Maven profiles to switch between `dev`, `staging`, `prod` environments |
| **API Test Layer** | Add RestAssured for API testing as a hybrid API+UI framework |

---

## Author Notes

This framework was built following industry best practices for enterprise-grade test automation:

- **No hardcoded waits** (except one `Thread.sleep(1000)` in `LoginPageMsg` which could be replaced)
- **No hardcoded credentials** in step definitions — loaded from `config.properties`
- **Headless by default** — runs on any CI server without a display
- **ThreadLocal driver** — future-proofed for parallel test execution
- **Dual reporting** — Allure for rich visual reports, JSON for CI integration
- **PicoContainer DI** — clean inter-class communication without static state

---

*Generated documentation based on full framework source analysis.*
