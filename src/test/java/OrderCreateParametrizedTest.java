import bodyclasses.Constants;
import bodyclasses.request.OrderCancel;
import bodyclasses.request.OrderCreate;
import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


@RunWith(Parameterized.class)
public class OrderCreateParametrizedTest {
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
    private ArrayList<String> color;

    public OrderCreateParametrizedTest(ArrayList<String> color) {
            this.color = color;
    }
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new ArrayList<>(Arrays.asList("BLACK"))},
                {new ArrayList<>(Arrays.asList("BLACK", "GREY"))},
                {new ArrayList<>(Arrays.asList("GREY"))},
                {new ArrayList<>()} // Пустой список
        });
    }

    @Test
    @Description("Можно выбрать любой вариант цвета")
    public void orderColor(){
        RestAssured.baseURI = Constants.BASE_URL;
        OrderCreate order = new OrderCreate(firstName,lastName,address,metroStation,phone,rentTime,deliveryDate,comment,color);
        Response response = OrderCreate.sendOrderCreate(order);
        OrderCreate.compareSuccessResponse(response);
        Integer _track = OrderCreate.getTrack(order);
        if(_track != null){
            String track = String.valueOf(_track);
            OrderCancel.sendRequestOrderCancel(track);
        }
    }
}