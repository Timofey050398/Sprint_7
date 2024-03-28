package bodyclasses.request;

import bodyclasses.Constants;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class OrderAccept {
    @Step("Send PUT api/v1/orders/accept/{orderId}?courierId={courierId}")
    public static Response sendOrderAccept(int courierId, String orderId){
        Response response = given()
                .queryParam("courierId",courierId)
                .put(Constants.ACCEPT_ORDER+"/{orderId}",orderId);
        return response;
    }
    @Step("Send PUT api/v1/orders/accept/{orderId}(without courierId)")
    public static Response sendOrderAccept(String orderId){
        Response response = given()
                .put(Constants.ACCEPT_ORDER+"/{orderId}",orderId);
        return response;
    }
    @Step("Send PUT api/v1/orders/accept}?courierId={courierId}(without orderId)")
    public static Response sendOrderAccept(int courierId){
        Response response = given()
                .queryParam("courierId",courierId)
                .put(Constants.ACCEPT_ORDER);
        return response;
    }
    @Step("Compare response with success response")
    public static  void compareResponseWithSuccess(Response response){
        response.then()
                .assertThat()
                .body("ok", equalTo(true))
                .and()
                .statusCode(200);
    }
    @Step("Compare response with wrong id")
    public static  void compareResponseWithWrongId(Response response){
        response.then()
                .assertThat()
                .body("message", equalTo("Заказа с таким id не существует"))
                .and()
                .statusCode(404);
    }
    @Step("Compare response with wrong courierId")
    public static  void compareResponseWithWrongCourier(Response response){
        response.then()
                .assertThat()
                .body("message", equalTo("Курьера с таким id не существует"))
                .and()
                .statusCode(404);
    }
    @Step("Compare response with correct code400 responce")
    public static  void compareCode400Response(Response response){
        response.then()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для поиска"))
                .and()
                .statusCode(400);
    }
}
