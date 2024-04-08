package bodyclasses.request;

import bodyclasses.Constants;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierDelete {
    @Step("Send correct DELETE /api/v1/courier/{id} request")
    public static Response sendDeleteCourier(String id){
        Response response= given().delete(Constants.CREATE_COURIER + "/{id}", id);
        return response;
    }
}
