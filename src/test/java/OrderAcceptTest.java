import bodyclasses.Constants;
import bodyclasses.request.*;
import bodyclasses.response.getorderbytrack.GetOrderByTrack;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Random;
import static java.lang.String.valueOf;

public class OrderAcceptTest {
    Random random = new Random();
    String login = valueOf(random.nextInt(1000000));
    String password = valueOf(random.nextInt(10000));
    String firstName = "Andrey";
    String lastName = "12345";
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
    public void  SuccessResponseOkTrue(){
        int id = CourierLogin.getId(login,password);
        int track = OrderCreate.getTrack(order);
        String orderId = GetOrderByTrack.getOrderId(track);
        Response response = OrderAccept.sendOrderAccept(id,orderId);
        OrderAccept.compareResponseWithSuccess(response);
    }
    @Test
    public void ErrorWhenCourierIdIsEmpty(){
        int track = OrderCreate.getTrack(order);
        String orderId = GetOrderByTrack.getOrderId(track);
        Response response = OrderAccept.sendOrderAccept(orderId);
        OrderAccept.compareCode400Response(response);
    }
    @Test
    public void ErrorWhenCourierIdIsWrong(){
        int id = CourierLogin.getId(login,password)*10;
        int track = OrderCreate.getTrack(order);
        String orderId = GetOrderByTrack.getOrderId(track);
        Response response = OrderAccept.sendOrderAccept(id,orderId);
        OrderAccept.compareResponseWithWrongCourier(response);
    }
    @Test
    public void ErrorWhenOrderIdIsEmpty(){
        int id = CourierLogin.getId(login,password);
        Response response = OrderAccept.sendOrderAccept(id);
        OrderAccept.compareCode400Response(response);
        System.out.println(response.getBody().asString());
    }
    @Test
    public void ErrorWhenOrderIdIsWrong(){
        int id = CourierLogin.getId(login,password);
        int track = OrderCreate.getTrack(order);
        String orderId = GetOrderByTrack.getOrderId(track)+"000";
        Response response = OrderAccept.sendOrderAccept(id,orderId);
        OrderAccept.compareResponseWithWrongId(response);
    }
}
