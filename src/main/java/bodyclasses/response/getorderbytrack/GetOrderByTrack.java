package bodyclasses.response.getorderbytrack;

import bodyclasses.Constants;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetOrderByTrack {
    private OrderObject order;

    public OrderObject getOrder() {
        return order;
    }

    public void setOrder(OrderObject order) {
        this.order = order;
    }
    @Step("get order id from GET api/v1/orders/track?t={track}")
    public static String getOrderId(int track){
        int order_id = given()
                .queryParam("t",track)
                .get(Constants.GET_ORDER)
                .then().extract().body().path("order.id");
        String orderId = String.valueOf(order_id);
        return orderId;
    }
    @Step("get response from GET api/v1/orders/track?t={track}")
    public static Response sendGetOrderByTrack(int track){
        Response response = given()
                .queryParam("t",track)
                .get(Constants.GET_ORDER);
        return response;
    }
}
