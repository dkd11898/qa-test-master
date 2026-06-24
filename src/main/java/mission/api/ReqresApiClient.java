package mission.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;

import java.util.Map;

public class ReqresApiClient {

    private static final String BASE_URL = "https://reqres.in/api";

    public Response getUsers(int page) {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .param("page", page)
                .when()
                .get("/users");
    }

    public Response getUser(int userId) {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .when()
                .get(String.format("/users/%d", userId));
    }

    public Response createUser(String name, String job) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", name);
        requestBody.put("job", job);

        return RestAssured.given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post("/users");
    }

    public Response login(String email, String password) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", email);
        requestBody.put("password", password);

        return RestAssured.given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post("/login");
    }
}
