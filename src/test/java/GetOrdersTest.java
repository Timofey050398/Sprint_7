import bodyclasses.Constants;
import bodyclasses.request.OrderCancel;
import bodyclasses.request.OrderCreate;
import bodyclasses.response.getorders.GetOrders;
import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class GetOrdersTest {

    Faker faker = new Faker();
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
    @Description("Тело ответа содержит список заказов")
    public void responseBodyHasOrderList(){
        Response response = GetOrders.sendGetOrder();
        GetOrders.doesResponseBodyHasOrders(response);
    }
    @Test
    @Description("При успешном запросе код ответа - 200")
    public void successCodeIs200(){
        Response response = GetOrders.sendGetOrder();
        response.then().statusCode(200);
    }
}
