import bodyclasses.Constants;
import bodyclasses.request.OrderCancel;
import bodyclasses.request.OrderCreate;
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
import java.util.concurrent.TimeUnit;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetOrderByTrackTest {
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
    public void successAnswerIsCorrect(){
        int track = OrderCreate.getTrack(order);
        GetOrderByTrack getOrderBody = GetOrderByTrack.sendGetOrderByTrack(track)
                .body()
                .as(GetOrderByTrack.class);
        assertThat(getOrderBody,notNullValue());
    }
    @Test
    public void successCodeIs200(){
        int track = OrderCreate.getTrack(order);
        Response response = GetOrderByTrack.sendGetOrderByTrack(track);
        response.then()
                .statusCode(200);
    }
    @Test
    public void errorWhenRequestWithoutTrack(){
        Response response = given().get(Constants.GET_ORDER);
        response.then()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для поиска"))
                .and()
                .statusCode(400);
    }
    @Test
    public void errorWhenTrackIsNotFound(){
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
