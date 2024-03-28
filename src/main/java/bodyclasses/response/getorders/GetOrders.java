package bodyclasses.response.getorders;
import bodyclasses.Constants;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.List;
import static org.junit.Assert.assertTrue;
import static io.restassured.RestAssured.given;

public class GetOrders {
    private List<Order> orders;
    private PageInfo pageInfo;
    private List<AvailableStations> availableStations;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<AvailableStations> getAvailableStations() {
        return availableStations;
    }

    public void setAvailableStations(List<AvailableStations> availableStations) {
        this.availableStations = availableStations;
    }
    @Step("Send request GET /api/v1/orders")
    public static Response sendGetOrder(){
        Response response =given()
                .get(Constants.ORDERS);
        return response;
    }
    @Step("Check to see if there is an order in the body of the response")
    public static void doesResponseBodyHasOrders(Response response){
        GetOrders getOrders = response.body().as(GetOrders.class);
        List<Order> orders = getOrders.getOrders();
        assertTrue(orders.size() > 0);
    }
}
