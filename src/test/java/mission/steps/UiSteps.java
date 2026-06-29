package mission.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import mission.driver.DriverManager;
import mission.pages.CheckoutInfoPage;
import mission.pages.CheckoutOverviewPage;
import mission.pages.HomePage;
import mission.pages.SauceDemoCartPage;
import mission.pages.SauceDemoInventoryPage;

import java.util.Map;

import static org.testng.Assert.assertEquals;

public class UiSteps {

    private HomePage homePage;
    private SauceDemoInventoryPage inventoryPage;
    private SauceDemoCartPage cartPage;
    private CheckoutInfoPage checkoutInfoPage;
    private CheckoutOverviewPage checkoutOverviewPage;

    @Given("^I am on the home page$")
    public void iAmOnTheHomePage() {
        homePage = new HomePage(DriverManager.getDriver());
        homePage.openHomePage();
    }

    @Given("^I login in with the following details$")
    public void iLoginInWithTheFollowingDetails(DataTable dt) {
        Map<String, String> row = dt.asMaps().get(0);
        homePage.login(row.get("userName"), row.get("Password"));
        inventoryPage = new SauceDemoInventoryPage(DriverManager.getDriver());
    }

    @And("^I add the following items to the basket$")
    public void iAddTheFollowingItemsToTheBasket(DataTable dt) {
        inventoryPage.addItemsToCart(dt.asList());
    }

    @And("^I  should see (\\d+) items added to the shopping cart$")
    public void iShouldSeeItemsAddedToTheShoppingCart(int itemCount) {
        assertEquals(inventoryPage.getCartItemCount(), itemCount);
    }

    @And("^I click on the shopping cart$")
    public void iClickOnTheShoppingCart() {
        inventoryPage.openCart();
        cartPage = new SauceDemoCartPage(DriverManager.getDriver());
    }

    @And("^I verify that the QTY count for each item should be 1$")
    public void iVerifyThatTheQTYCountForEachItemShouldBe1() {
        cartPage.verifyEachItemQuantityOne();
    }

    @And("^I remove the following item:$")
    public void iRemoveTheFollowingItem(DataTable dt) {
        cartPage.removeItem(dt.asList().get(0));
    }

    @And("^I click on the CHECKOUT button$")
    public void iClickOnTheCHECKOUTButton() {
        cartPage.clickCheckout();
        checkoutInfoPage = new CheckoutInfoPage(DriverManager.getDriver());
    }

    @And("^I type \"([^\"]*)\" for First Name$")
    public void iTypeForFirstName(String firstName) {
        checkoutInfoPage.fillCustomerInformation(firstName, null, null);
    }

    @And("^I type \"([^\"]*)\" for Last Name$")
    public void iTypeForLastName(String lastName) {
        checkoutInfoPage.fillCustomerInformation(null, lastName, null);
    }

    @And("^I type \"([^\"]*)\" for ZIP/Postal Code$")
    public void iTypeForZIPPostalCode(String postalCode) {
        checkoutInfoPage.fillCustomerInformation(null, null, postalCode);
    }

    @When("^I click on the CONTINUE button$")
    public void iClickOnTheCONTINUEButton() {
        checkoutInfoPage.clickContinue();
        checkoutOverviewPage = new CheckoutOverviewPage(DriverManager.getDriver());
    }

    @Then("^Item total will be equal to the total of items on the list$")
    public void itemTotalWillBeEqualToTheTotalOfItemsOnTheList() {
        checkoutOverviewPage.verifyItemTotal();
    }

    @And("^a Tax rate of 8 % is applied to the total$")
    public void aTaxRateOf8IsAppliedToTheTotal() {
        checkoutOverviewPage.verifyTaxRate(8.0);
    }
}
