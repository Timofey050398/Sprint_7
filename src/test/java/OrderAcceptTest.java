import bodyclasses.Constants;
import bodyclasses.request.*;
import bodyclasses.response.getorderbytrack.GetOrderByTrack;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrderAcceptTest {
    CourierCreate courier = new CourierCreate();

    OrderCreate order = new OrderCreate();
    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
        CourierCreate.sendPostCourierCreate(courier);
        OrderCreate.sendOrderCreate(order);
    }
    @After
    @Description("Delete Courier")
    public void deleteCourierAndOrder(){
        Integer _id = CourierLogin.getId(courier.getLogin(),courier.getPassword());
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
        int id = CourierLogin.getId(courier.getLogin(),courier.getPassword());
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
        int id = CourierLogin.getId(courier.getLogin(),courier.getPassword())*10;
        int track = OrderCreate.getTrack(order);
        String orderId = GetOrderByTrack.getOrderId(track);
        Response response = OrderAccept.sendOrderAccept(id,orderId);
        OrderAccept.compareResponseWithWrongCourier(response);
    }
    @Test
    public void errorWhenOrderIdIsEmpty(){
        int id = CourierLogin.getId(courier.getLogin(),courier.getPassword());
        Response response = OrderAccept.sendOrderAccept(id);
        OrderAccept.compareCode400Response(response);
    }
    @Test
    public void errorWhenOrderIdIsWrong(){
        int id = CourierLogin.getId(courier.getLogin(),courier.getPassword());
        int track = OrderCreate.getTrack(order);
        String orderId = GetOrderByTrack.getOrderId(track)+"000";
        Response response = OrderAccept.sendOrderAccept(id,orderId);
        OrderAccept.compareResponseWithWrongId(response);
    }
}
