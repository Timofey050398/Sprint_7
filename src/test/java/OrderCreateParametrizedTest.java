import bodyclasses.Constants;
import bodyclasses.request.OrderCancel;
import bodyclasses.request.OrderCreate;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class OrderCreateParametrizedTest {
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
        OrderCreate order = new OrderCreate(color);
        Response response = OrderCreate.sendOrderCreate(order);
        OrderCreate.compareSuccessResponse(response);
        Integer _track = OrderCreate.getTrack(order);
        if(_track != null){
            String track = String.valueOf(_track);
            OrderCancel.sendRequestOrderCancel(track);
        }
    }
}