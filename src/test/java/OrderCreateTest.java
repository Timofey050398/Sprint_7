import bodyclasses.Constants;
import bodyclasses.request.OrderCancel;
import bodyclasses.request.OrderCreate;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class OrderCreateTest {

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
    @Description("Можно указать серый цвет")
    public void OrderColorCanBeGrey(){
        color.add("GREY");
        order.setColor(color);
        Response response = OrderCreate.sendOrderCreate(order);
        OrderCreate.compareSuccessResponse(response);
    }
    @Test
    @Description("Можно указать черный цвет")
    public void OrderColorCanBeBlack(){
        color.add("BLACK");
        order.setColor(color);
        Response response = OrderCreate.sendOrderCreate(order);
        OrderCreate.compareSuccessResponse(response);
    }
    @Test
    @Description("Можно указать оба цвета")
    public void OrderColorCanBeBoth(){
        color.add("BLACK");
        color.add("GREY");
        order.setColor(color);
        Response response = OrderCreate.sendOrderCreate(order);
        OrderCreate.compareSuccessResponse(response);
    }
    @Test
    @Description("Можно не указывать цвет")
    public void OrderColorKeyCanBeEmpty(){
        Response response = OrderCreate.sendOrderCreate(order);
        OrderCreate.compareSuccessResponse(response);
    }
    @Test
    @Description("Параметр color не обязательный")
    public void OrderColorParamCanBeEmpty(){
        OrderCreate orderWithoutColor = new OrderCreate(firstName,lastName,address,metroStation,phone,rentTime,deliveryDate,comment);
        Response response = OrderCreate.sendOrderCreate(orderWithoutColor);
        Integer _track = OrderCreate.getTrack(orderWithoutColor);
        if(_track != null){
            String track = String.valueOf(_track);
            OrderCancel.sendRequestOrderCancel(track);}
        OrderCreate.compareSuccessResponse(response);
    }
    @Test
    @Description("При успешном ответе тело содержит трек")
    public void responseBodyHasTrack(){
        Response response = OrderCreate.sendOrderCreate(order);
        OrderCreate.compareSuccessResponseBody(response);
    }
}