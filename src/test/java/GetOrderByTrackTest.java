import bodyclasses.Constants;
import bodyclasses.request.OrderCancel;
import bodyclasses.request.OrderCreate;
import bodyclasses.response.getorderbytrack.GetOrderByTrack;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetOrderByTrackTest {
    String firstName = "Andrey";
    String lastName = "Uchiha";
    String address = "Konoha, 142 apt.";
    int  metroStation = 4;
    String phone = "+7 800 355 35 35";
    int rentTime = 5;
    String deliveryDate = "2020-06-06";
    String comment="Saske, come back to Konoha";
    ArrayList<String> color = new ArrayList<>();
    OrderCreate order = new OrderCreate(firstName,lastName,address,metroStation,phone,rentTime,deliveryDate,comment,color);
    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
        OrderCreate.sendOrderCreate(order);
    }
    @After
    @Description("Delete Order")
    public void deleteOrder(){
        Integer _track = OrderCreate.getTrack(order);
        if(_track != null){
            String track = String.valueOf(_track);
            OrderCancel.sendRequestOrderCancel(track);
        }
    }
    @Test
    public void SuccessAnswerIsCorrect(){
        int track = OrderCreate.getTrack(order);
        GetOrderByTrack getOrderBody = GetOrderByTrack.sendGetOrderByTrack(track)
                .body()
                .as(GetOrderByTrack.class);
        assertThat(getOrderBody,notNullValue());
    }
    @Test
    public void SuccessCodeIs200(){
        int track = OrderCreate.getTrack(order);
        Response response = GetOrderByTrack.sendGetOrderByTrack(track);
        response.then()
                .statusCode(200);
    }
    @Test
    public void ErrorWhenRequestWithoutTrack(){
        Response response = given().get(Constants.GET_ORDER);
        response.then()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для поиска"))
                .and()
                .statusCode(400);
    }
    @Test
    public void ErrorWhenTrackIsNotFound(){
        int track = OrderCreate.getTrack(order);
        track = track*100;
        Response response = GetOrderByTrack.sendGetOrderByTrack(track);
        response.then()
                .assertThat()
                .body("message", equalTo("Заказ не найден"))
                .and()
                .statusCode(404);
    }
}
