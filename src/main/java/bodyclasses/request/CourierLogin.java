package bodyclasses.request;
import bodyclasses.Constants;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.isA;

public class CourierLogin {
    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CourierLogin() {
        // Пустой конструктор
    }

    public CourierLogin(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public CourierLogin(String login) {
        this.login = login;
    }

    @Step("Send correct POST /api/v1/courier/login request")
    public static Response sendPostCourierLogin(CourierLogin courier){
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(Constants.COURIER_LOGIN);
        return response;
    }
    @Step("Send incorrect POST /api/v1/courier/login request")
    public static Response sendPostCourierLogin(HashMap<String, Object> requestBody){
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post(Constants.COURIER_LOGIN);
        return response;
    }
    @Step("Get id from response POST /api/v1/courier/login request")
    public static Integer getId(String login, String password){
        CourierLogin courierLogin = new CourierLogin(login,password);
        Response response = sendPostCourierLogin(courierLogin);
        Integer id = response
                .then()
                .extract()
                .body()
                .path("id");
        return id;
    }
    @Step("Checks if the response is correct on a successful request")
    public static void compareSuccessResponse(Response response){
        response.then()
                .assertThat()
                .body("id", isA(Integer.class))
                .and()
                .statusCode(200);
    }
    @Step("Checks if the response is correct when querying with Insufficient data for search")
    public static void compareCode400Response(Response response){
        response.then()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }
    @Step("Checks if the response is correct when querying with Insufficient data for search")
    public static void compareResponseHasId(Response response){
        response.then()
                .assertThat()
                .body("id", notNullValue())
                .and()
                .statusCode(200);
    }
    @Step("Checks if the response is correct for a query with a non-existent id")
    public static void compareIdNotFoundResponse(Response response){
        response.then()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }
}
