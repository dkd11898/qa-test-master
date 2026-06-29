package mission.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import mission.api.ReqresApiClient;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class ApiSteps {

    private final ReqresApiClient apiClient = new ReqresApiClient();
    private Response response;
    private int totalUsersAcrossPages;

    @Given("^I get the default list of users for on 1st page$")
    public void iGetTheDefaultListofusers() {
        response = apiClient.getUsers(1);
        response.then().statusCode(200);
    }

    @When("I get the list of all users within every page")
    public void iGetTheListOfAllUsers() {
        int totalPages = response.jsonPath().getInt("total_pages");
        int totalCount = 0;
        for (int page = 1; page <= totalPages; page++) {
            Response pageResponse = apiClient.getUsers(page);
            pageResponse.then().statusCode(200);
            totalCount += pageResponse.jsonPath().getList("data.id").size();
        }
        totalUsersAcrossPages = totalCount;
        Assert.assertTrue(totalCount > 0);
    }

    @Then("I should see total users count equals the number of user ids")
    public void iShouldMatchTotalCount() {
        int total = response.jsonPath().getInt("total");
        Assert.assertEquals(total, totalUsersAcrossPages);
    }

    @Given("^I make a search for user (.*)$")
    public void iMakeASearchForUser(String sUserID) {
        response = apiClient.getUser(Integer.parseInt(sUserID));
    }

    @Then("I should see the following user data")
    public void iShouldSeeFollowingUserData(DataTable dt) {
        response.then().statusCode(200);
        Map<String, String> expected = dt.asMaps().get(0);
        Assert.assertEquals(response.jsonPath().getString("data.first_name"), expected.get("first_name"));
        Assert.assertEquals(response.jsonPath().getString("data.email"), expected.get("email"));
    }

    @Then("^I receive error code (.*) in response$")
    public void iReceiveErrorCodeInResponse(int responseCode) {
        Assert.assertEquals(response.statusCode(), responseCode);
    }

    @Given("^I create a user with following (.*) (.*)$")
    public void iCreateUserWithFollowing(String sUsername, String sJob) {
        response = apiClient.createUser(sUsername, sJob);
    }

    @Then("response should contain the following data")
    public void responseShouldContainTheFollowingData(DataTable dt) {
        response.then().statusCode(201);
        Map<String, String> expected = dt.asMaps().get(0);
        Assert.assertEquals(response.jsonPath().getString("name"), expected.get("name"));
        Assert.assertEquals(response.jsonPath().getString("job"), expected.get("job"));
        Assert.assertNotNull(response.jsonPath().getString("id"));
        Assert.assertNotNull(response.jsonPath().getString("createdAt"));
    }

    @Given("I login unsuccessfully with the following data")
    public void iLoginSuccesfullyWithFollowingData(DataTable dt) {
        Map<String, String> credentials = dt.asMaps().get(0);
        response = apiClient.login(credentials.get("Email"), credentials.get("Password"));
    }

    @Then("^I should get a response code of (\\d+)$")
    public void iShouldGetAResponseCodeOf(int responseCode) {
        Assert.assertEquals(response.statusCode(), responseCode);
    }

    @And("^I should see the following response message:$")
    public void iShouldSeeTheFollowingResponseMessage(DataTable dt) {
        Map<String, String> expected = dt.asMaps().get(0);
        String body = response.getBody().asString();
        expected.values().forEach(value -> Assert.assertTrue(body.contains(value)));
    }

    @Given("^I wait for the user list to load$")
    public void iWaitForUserListToLoad() {
        response = apiClient.getUsers(3);
        response.then().statusCode(200);
    }

    @Then("I should see that every user has a unique id")
    public void iShouldSeeThatEveryUserHasAUniqueID() {
        List<Integer> ids = response.jsonPath().getList("data.id");
        long uniqueCount = ids.stream().distinct().count();
        Assert.assertEquals(ids.size(), uniqueCount);
    }
}
