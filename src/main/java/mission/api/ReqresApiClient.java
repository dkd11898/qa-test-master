package mission.api;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import mission.config.ConfigReader;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class ReqresApiClient {

    public Response getUsers(int page) {
        return baseRequest()
                .param("page", page)
                .when()
                .get("/users");
    }

    public Response getUser(int userId) {
        return baseRequest()
                .when()
                .get(String.format("/users/%d", userId));
    }

    public Response createUser(String name, String job) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", name);
        requestBody.put("job", job);

        return baseRequest()
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post("/users");
    }

    public Response login(String email, String password) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", email);
        requestBody.put("password", password);

        return baseRequest()
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post("/login");
    }

    private RequestSpecification baseRequest() {
        return given()
                .baseUri(ConfigReader.getProperty("api.baseUrl"))
                .header("x-api-key", ConfigReader.getProperty("api.key"));
    }
}
