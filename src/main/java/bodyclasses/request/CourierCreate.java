package bodyclasses.request;

import bodyclasses.Constants;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CourierCreate{
    private  String login;
    private  String password;
    private String firstName;

    public  String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public  String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public CourierCreate() {
        Faker faker = new Faker();
        this.login = faker.name().username();
        this.password = faker.internet().password();
        this.firstName = faker.name().firstName();
    }

    public CourierCreate(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public CourierCreate(String login, String password) {
        this.login = login;
        this.password = password;
    }
    @Step("Send correct POST /api/v1/courier request")
    public static Response sendPostCourierCreate(CourierCreate courier){
        Response response =given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(Constants.CREATE_COURIER);
        return response;
    }
    @Step("Send incorrect POST /api/v1/courier request")
    public static Response sendPostCourierCreate(HashMap<String, Object> requestBody){
        Response response =given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post(Constants.CREATE_COURIER);
        return response;
    }
    @Step("Compare response with correct code400 responce")
    public static  void compareCode400Response(Response response){
        response.then()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }
    @Step("Checks if the response is correct for a request with a login already used")
    public static void compareResponseWithLoginAlreadyUsed(Response response){
        response.then()
                .assertThat()
                .body("message", equalTo("Этот логин уже используется"))
                .and()
                .statusCode(409);
    }
    @Step("Checks if the response is correct for a request with correct data")
    public static void compareWithCorrectCreatingResponse(Response response){
        response.then()
                .assertThat()
                .body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }
}
