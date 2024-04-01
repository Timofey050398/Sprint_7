import bodyclasses.Constants;
import bodyclasses.request.*;
import bodyclasses.response.getorderbytrack.GetOrderByTrack;
import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.lang.String.valueOf;

public class OrderAcceptTest {
    Faker faker = new Faker();
    String login = faker.name().username();
    String password = faker.internet().password();
    String firstName = faker.name().firstName();
    String lastName = faker.name().lastName();
    String address = faker.address().fullAddress();
    int  metroStation = faker.number().numberBetween(1,30);
    String phone = faker.phoneNumber().phoneNumber();
    int rentTime = faker.number().numberBetween(1,7);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String deliveryDate = formatter.format(faker.date().future(90, TimeUnit.DAYS));
    String comment= faker.lorem().sentence();
    ArrayList<String> color = new ArrayList<>();
    OrderCreate order = new OrderCreate(firstName,lastName,address,metroStation,phone,rentTime,deliveryDate,comment,color);
    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
        CourierCreate courier = new CourierCreate(login,password,firstName);
        CourierCreate.sendPostCourierCreate(courier);
        OrderCreate.sendOrderCreate(order);
    }
    @After
    @Description("Delete Courier")
    public void deleteCourierAndOrder(){
        Integer _id = CourierLogin.getId(login,password);
        if(_id != null){
            String id = String.valueOf(_id);
            CourierDelete.sendDeleteCourier(id);
        }
        Integer _track = OrderCreate.getTrack(order);
        if(_track != null){
            String track = String.valueOf(_track);
            OrderCancel.sendRequestOrderCancel(track);
        }
    }
    @Test
    public void  successResponseOkTrue(){
        int id = CourierLogin.getId(login,password);
        int track = OrderCreate.getTrack(order);
        String orderId = GetOrderByTrack.getOrderId(track);
        Response response = OrderAccept.sendOrderAccept(id,orderId);
        OrderAccept.compareResponseWithSuccess(response);
    }
    @Test
    public void errorWhenCourierIdIsEmpty(){
        int track = OrderCreate.getTrack(order);
        String orderId = GetOrderByTrack.getOrderId(track);
        Response response = OrderAccept.sendOrderAccept(orderId);
        OrderAccept.compareCode400Response(response);
    }
    @Test
    public void errorWhenCourierIdIsWrong(){
        int id = CourierLogin.getId(login,password)*10;
        int track = OrderCreate.getTrack(order);
        String orderId = GetOrderByTrack.getOrderId(track);
        Response response = OrderAccept.sendOrderAccept(id,orderId);
        OrderAccept.compareResponseWithWrongCourier(response);
    }
    @Test
    public void errorWhenOrderIdIsEmpty(){
        int id = CourierLogin.getId(login,password);
        Response response = OrderAccept.sendOrderAccept(id);
        OrderAccept.compareCode400Response(response);
    }
    @Test
    public void errorWhenOrderIdIsWrong(){
        int id = CourierLogin.getId(login,password);
        int track = OrderCreate.getTrack(order);
        String orderId = GetOrderByTrack.getOrderId(track)+"000";
        Response response = OrderAccept.sendOrderAccept(id,orderId);
        OrderAccept.compareResponseWithWrongId(response);
    }
}
