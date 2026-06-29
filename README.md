# QA Automation Framework

Cucumber + TestNG + Selenium + REST Assured automation for:

- **API-Test.feature** against [https://reqres.in](https://reqres.in)
- **UI-Test.feature** against [https://www.saucedemo.com](https://www.saucedemo.com)

## 1. Prerequisites

- Java 25 LTS (or higher) on `PATH`, with `JAVA_HOME` set
- Maven 3.9+
- Chrome (default browser; Edge/Firefox also supported, see Configuration)
- A free [reqres.in](https://reqres.in) account and API key (see Configuration — every request now requires `x-api-key`)

## 2. Configuration

All environment/test data lives in `src/test/java/TestData/TestData.properties` — nothing is hardcoded in source:

| Property             | Purpose                                              |
|-----------------------|-------------------------------------------------------|
| `Browser`             | `Chrome` \| `Edge` \| `Firefox` \| `ChromeHeadless`    |
| `ui.baseUrl`          | Base URL for the SauceDemo UI suite                   |
| `api.baseUrl`         | Base URL for the reqres.in API suite                  |
| `api.key`             | Your reqres.in `x-api-key` — sign up free at reqres.in |
| `ScreenshotLocation`  | Where UI-scenario screenshots are written              |

`reqres.in` introduced mandatory per-account API keys after this assignment's starter code was written (every endpoint now requires an `x-api-key` header). Sign up at reqres.in, copy your key into `api.key`, and the API suite will run against the real service. Without a valid key every API scenario will fail with `401 Unauthorized` — this is an external service requirement, not a framework defect.

## 3. Running the tests

```powershell
mvn clean test
```

Scenarios run across 4 threads in parallel (`dataproviderthreadcount=4` in `pom.xml`, `@DataProvider(parallel = true)` in `RunnerTest`). Each scenario gets its own browser session via a thread-scoped `WebDriver` — no shared mutable state across threads.

Reports are written to `test-output/cucumber-reports/` (pretty text, HTML, JSON, rerun file) and `target/surefire-reports/` (TestNG results).

## 4. Package structure

```
src/main/java/mission/
  api/      ReqresApiClient        - dedicated REST client, one method per endpoint
  config/   ConfigReader           - loads TestData.properties once, fails fast on missing keys
  driver/   DriverFactory          - creates a WebDriver for a given browser name
            DriverManager          - thread-scoped WebDriver storage
  pages/    BasePage               - shared click/type/wait helpers, PageFactory init
            HomePage, SauceDemoInventoryPage, SauceDemoCartPage,
            CheckoutInfoPage, CheckoutOverviewPage

src/test/java/mission/
  hooks/    Hook                   - drives DriverFactory/DriverManager, screenshots on @ui scenarios
  runner/   RunnerTest             - Cucumber + TestNG entry point, parallel DataProvider
  steps/    UiSteps, ApiSteps      - orchestration only: build page objects/API client, call business methods
```

Step definitions never touch Selenium directly — no `By.xpath(...)`, no `driver.findElement(...)`, no browser setup. They only call page-object/business methods (`homePage.login(...)`, `inventoryPage.addItemsToCart(...)`).

## 5. Design decisions

- **Driver lifecycle is centralized.** `Hook` (the only place a browser is created or quit) asks `DriverFactory` for a `WebDriver` and stores it in `DriverManager`, a `ThreadLocal`. Page objects never create a driver — they receive one via constructor injection (`new HomePage(DriverManager.getDriver())`), so the dependency flow is hook → manager → step → page object.
- **Shared wait/action layer.** `BasePage` centralizes `click`, `type`, `waitUntilVisible/Clickable/Present`, and a generic `waitUntil(condition)` so page classes never duplicate raw Selenium waits — locators stay declarative (`@FindBy`) and page methods stay focused on business behavior.
- **Config-driven, no hardcoded endpoints.** Both the UI base URL and the API base URL/key come from `ConfigReader`, not from constants buried in client/page classes.
- **API client is a real service layer**, not request calls inlined in steps — one method per reqres.in operation, with the `x-api-key` header and base URI applied once in `baseRequest()`.
- **Parallel execution.** Migrated the whole Cucumber stack from the legacy `info.cukes` 1.2.5 line to `io.cucumber` 7.18.1 — the older runtime's formatter/plugin plumbing isn't thread-safe and threw `ConcurrentModificationException` under TestNG's parallel `DataProvider`. The modern stack was built for this and runs cleanly across 4 threads.

## 6. Bugs found and fixed in the starter code

| Issue | Fix |
|---|---|
| `@DataProvider(parallel = false)` made the pom's `dataproviderthreadcount=4` a no-op | Enabled `parallel = true`; migrated to `io.cucumber` for genuine thread safety |
| `pom.xml` depended on `selenium-server:4.35.0`, which doesn't exist on Maven Central | Removed (unused — nothing references Grid/RemoteWebDriver) |
| Ancient Selenium 2.x artifacts (`selenium-htmlunit-driver`, `selenium:2.0rc2`) shadowed classes from the real `selenium-java:4.35.0` (e.g. `EdgeDriver` failed to resolve) | Removed, unused |
| `selenium-java` was scoped `test`, but main-source `BrowserSetup`/`DriverFactory` needs it at compile time | Removed the scope restriction |
| Selenium 4.11+ dropped the `(int, TimeUnit)` timeout overloads | Switched to `Duration`-based timeouts |
| `rest-assured:4.1.2`'s bundled Groovy can't initialize under JDK 25's module encapsulation | Upgraded to `rest-assured:5.5.2` |
| Three step regexes in `ApiSteps` lacked `^...$` anchors, which legacy Cucumber tolerated but Cucumber Expressions in `io.cucumber` does not (steps came back "undefined") | Anchored the regexes |
| `Assert.assertEquals(actual, expected)` args were reversed in several steps, producing backwards "expected/found" failure messages | Corrected argument order throughout `ApiSteps`/`UiSteps` |
| Cart "remove item" used a brittle exact-text XPath that intermittently couldn't locate the row | Switched to the same stable `id="remove-<slug>"` convention already used for add-to-cart, via a shared `slugify()` helper |
| Cart item count was read immediately after the last add-to-cart click with no synchronization, causing an intermittent off-by-one | `addItemsToCart` now waits until the badge actually reflects the expected count before returning |
| `SampleTest.feature` / `SamplePage` / `iniClass` / empty `StepDefinition.java` were dead starter scaffolding unrelated to the two required features, and the missing `@ui` tag on `SampleTest.feature` caused a null-driver `NullPointerException` | Removed — out of scope for the two required feature files |

## 7. Scalability notes

- New pages/endpoints extend `BasePage`/`ReqresApiClient` without touching hook, runner, or driver-factory code (open/closed).
- New browsers are added in `DriverFactory` only; nothing else changes.
- Switching environments is a one-line edit in `TestData.properties` (or could be swapped for an env-var-driven `ConfigReader` if multiple environments are needed later).
