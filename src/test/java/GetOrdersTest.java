import bodyclasses.Constants;
import bodyclasses.request.OrderCancel;
import bodyclasses.request.OrderCreate;
import bodyclasses.response.getorders.GetOrders;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class GetOrdersTest {


    OrderCreate order = new OrderCreate();
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
