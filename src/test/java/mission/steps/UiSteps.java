package mission.steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import mission.HomePage;
import mission.pages.CheckoutInfoPage;
import mission.pages.CheckoutOverviewPage;
import mission.pages.SauceDemoCartPage;
import mission.pages.SauceDemoInventoryPage;

import java.util.List;
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
        homePage = new HomePage();
        homePage.openHomePage();
    }

    @Given("^I login in with the following details$")
    public void iLoginInWithTheFollowingDetails(DataTable dt) {
        Map<String, String> row = dt.asMaps(String.class, String.class).get(0);
        homePage.login(row.get("userName"), row.get("Password"));
        inventoryPage = new SauceDemoInventoryPage();
    }

    @And("^I add the following items to the basket$")
    public void iAddTheFollowingItemsToTheBasket(DataTable dt) {
        List<String> items = dt.asList(String.class);
        inventoryPage.addItemsToCart(items);
    }

    @And("^I  should see (\\d+) items added to the shopping cart$")
    public void iShouldSeeItemsAddedToTheShoppingCart(int itemCount) {
        assertEquals(itemCount, inventoryPage.getCartItemCount());
    }

    @And("^I click on the shopping cart$")
    public void iClickOnTheShoppingCart() {
        inventoryPage.openCart();
        cartPage = new SauceDemoCartPage();
    }

    @And("^I verify that the QTY count for each item should be 1$")
    public void iVerifyThatTheQTYCountForEachItemShouldBe1() {
        cartPage.verifyEachItemQuantityOne();
    }

    @And("^I remove the following item:$")
    public void iRemoveTheFollowingItem(DataTable dt) {
        cartPage.removeItem(dt.asList(String.class).get(0));
    }

    @And("^I click on the CHECKOUT button$")
    public void iClickOnTheCHECKOUTButton() {
        cartPage.clickCheckout();
    }

    @And("^I type \"([^\"]*)\" for First Name$")
    public void iTypeForFirstName(String firstName) {
        if (checkoutInfoPage == null) {
            checkoutInfoPage = new CheckoutInfoPage();
        }
        checkoutInfoPage.fillCustomerInformation(firstName, null, null);
    }

    @And("^I type \"([^\"]*)\" for Last Name$")
    public void iTypeForLastName(String lastName) {
        if (checkoutInfoPage == null) {
            checkoutInfoPage = new CheckoutInfoPage();
        }
        checkoutInfoPage.fillCustomerInformation(null, lastName, null);
    }

    @And("^I type \"([^\"]*)\" for ZIP/Postal Code$")
    public void iTypeForZIPPostalCode(String postalCode) {
        if (checkoutInfoPage == null) {
            checkoutInfoPage = new CheckoutInfoPage();
        }
        checkoutInfoPage.fillCustomerInformation(null, null, postalCode);
    }

    @When("^I click on the CONTINUE button$")
    public void iClickOnTheCONTINUEButton() {
        if (checkoutInfoPage == null) {
            checkoutInfoPage = new CheckoutInfoPage();
        }
        checkoutInfoPage.clickContinue();
        checkoutOverviewPage = new CheckoutOverviewPage();
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
