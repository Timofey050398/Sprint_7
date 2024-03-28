package bodyclasses.request;

import bodyclasses.Constants;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderCancel {

    @Step("Send correct PUT /api/v1/courier/orders/cancel request")
    public static void sendRequestOrderCancel(String track){
            given()
                .header("Content-type", "application/json")
                .and()
                .body("{\"track\": "+track+"}")
                .when()
                .put(Constants.ORDERS+"/cancel");
    }
}
