package bodyclasses.request;
import bodyclasses.Constants;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.isA;

public class OrderCreate {
    private String firstName;
    private String lastName;
    private String address;
    private int metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private List<String> color;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMetroStation() {
        return metroStation;
    }

    public void setMetroStation(int metroStation) {
        this.metroStation = metroStation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRentTime() {
        return rentTime;
    }

    public void setRentTime(int rentTime) {
        this.rentTime = rentTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getColor() {
        return color;
    }

    public void setColor(List<String> color) {
        this.color = color;
    }
    public OrderCreate(){
        Faker faker = new Faker();
        this.firstName = faker.name().firstName();
        this.lastName = faker.name().lastName();
        this.address = faker.address().fullAddress();
        this.metroStation = faker.number().numberBetween(1,30);
        this.phone = faker.phoneNumber().phoneNumber();
        this.rentTime = faker.number().numberBetween(1,7);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.deliveryDate = formatter.format(faker.date().future(90, TimeUnit.DAYS));
        this.comment= faker.lorem().sentence();
        this.color = new ArrayList<>();
    }
    public OrderCreate(ArrayList<String> color){
        Faker faker = new Faker();
        this.firstName = faker.name().firstName();
        this.lastName = faker.name().lastName();
        this.address = faker.address().fullAddress();
        this.metroStation = faker.number().numberBetween(1,30);
        this.phone = faker.phoneNumber().phoneNumber();
        this.rentTime = faker.number().numberBetween(1,7);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.deliveryDate = formatter.format(faker.date().future(90, TimeUnit.DAYS));
        this.comment= faker.lorem().sentence();
        this.color =color;
    }
    public OrderCreate(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, ArrayList<String> color){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }
    public OrderCreate(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
    }
    @Step("Get track from POST /api/v1/courier/orders response")
    public static Integer getTrack(OrderCreate orderCreate){
        Integer track = sendOrderCreate(orderCreate)
                .then().extract().body().path("track");
        return track;
    }
    @Step("Send correct POST /api/v1/courier/orders request")
    public static Response sendOrderCreate(OrderCreate order){
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(Constants.ORDERS);
        return response;
    }

    @Step("Checks if the response body has track on a successful request")

    public static void compareSuccessResponseBody(Response response){
        response.then().assertThat().body("track", isA(Integer.class));
    }
    @Step("Checks if the response is correct on a successful request")

    public static void compareSuccessResponse(Response response){
        response.then()
                .assertThat()
                .body("track", isA(Integer.class))
                .and()
                .statusCode(201);
    }
}
