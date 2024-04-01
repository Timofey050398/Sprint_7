import bodyclasses.Constants;
import bodyclasses.request.CourierCreate;
import bodyclasses.request.CourierDelete;
import bodyclasses.request.CourierLogin;
import com.github.javafaker.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;


public class CourierCreateTest {
    Faker faker = new Faker();
    String login = faker.name().username();
    String password = faker.internet().password();
    String firstName = faker.name().firstName();
    HashMap<String, Object> requestBody = new HashMap<>();
    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
    }
    @After
    @Description("Delete Courier")
    public void deleteCourier(){
        Integer _id = CourierLogin.getId(login,password);
        if(_id != null){
            String id = String.valueOf(_id);
            CourierDelete.sendDeleteCourier(id);
        }
    }

    @Test
    @DisplayName("Успешное создание курьера")
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void correctCreateResponceMessage() {
        CourierCreate courier = new CourierCreate(login,password,firstName);
        Response response = CourierCreate.sendPostCourierCreate(courier);
        CourierCreate.compareWithCorrectCreatingResponse(response);
    }
    @Test
    @DisplayName("Eсли создать пользователя с логином, который уже есть, возвращается ошибка")
    @Description("Basic test for /api/v1/courier endpoint")
    public void cantCreateTwoCouriersWithEqualLogins() {
        String secondFirstName = faker.name().firstName();
        String secondPassword = faker.internet().password();
        CourierCreate firstCourier = new CourierCreate(login,password,firstName);
        CourierCreate secondCourier = new CourierCreate(login,secondPassword,secondFirstName);
        CourierCreate.sendPostCourierCreate(firstCourier);
        Response response = CourierCreate.sendPostCourierCreate(secondCourier);
        CourierCreate.compareResponseWithLoginAlreadyUsed(response);
    }
    @Test
    @DisplayName("Нельзя создать 2 курьера c одинаковым login, password и firstName")
    @Description("Basic test for /api/v1/courier endpoint")
    public void cantCreateTwoEqualsCouriers() {
        CourierCreate firstCourier = new CourierCreate(login,password,firstName);
        CourierCreate secondCourier = new CourierCreate(login,password,firstName);
        CourierCreate.sendPostCourierCreate(firstCourier);
        Response response = CourierCreate.sendPostCourierCreate(secondCourier);
        CourierCreate.compareResponseWithLoginAlreadyUsed(response);
    }
    @Test
    @DisplayName("Код ответа 400, если в запросе не передан параметр login")
    @Description("Basic test for /api/v1/courier endpoint")
    public void cantCreateCourierWithoutLogin() {
        requestBody.put("password",password);
        requestBody.put("firstName",firstName);
        Response response = CourierCreate.sendPostCourierCreate(requestBody);
        CourierCreate.compareCode400Response(response);
    }
    @Test
    @DisplayName("Код ответа 400, если в запросе не передан параметр password")
    @Description("Basic test for /api/v1/courier endpoint")
    public void cantCreateCourierWithoutPassword() {
        requestBody.put("login",login);
        requestBody.put("firstName",firstName);
        Response response = CourierCreate.sendPostCourierCreate(requestBody);
        CourierCreate.compareCode400Response(response);
    }
    @Test
    @DisplayName("Код ответа 400, если в запросе не передан параметр firstName")
    @Description("Basic test for /api/v1/courier endpoint")
    public void cantCreateCourierWithoutFirstName() {
        requestBody.put("login",login);
        requestBody.put("password",password);
        Response response = CourierCreate.sendPostCourierCreate(requestBody);
        CourierCreate.compareCode400Response(response);
    }
    @Test
    @DisplayName("Код ответа 400, если в запросе не передан ключ login")
    @Description("Basic test for /api/v1/courier endpoint")
    public void cantCreateCourierWithoutLoginKey() {
        CourierCreate courier = new CourierCreate("",password,firstName);
        Response response = CourierCreate.sendPostCourierCreate(courier);
        CourierCreate.compareCode400Response(response);
    }
    @Test
    @DisplayName("Код ответа 400, если в запросе не передан ключ password")
    @Description("Basic test for /api/v1/courier endpoint")
    public void cantCreateCourierWithoutPasswordKey() {
        CourierCreate courier = new CourierCreate(login,"",firstName);
        Response response = CourierCreate.sendPostCourierCreate(courier);
        CourierCreate.compareCode400Response(response);
    }
    @Test
    @DisplayName("Код ответа 400, если в запросе не передан ключ firstName")
    @Description("Basic test for /api/v1/courier endpoint")
    public void cantCreateCourierWithoutFirstNameKey() {
        CourierCreate courier = new CourierCreate(login,password,"");
        Response response = CourierCreate.sendPostCourierCreate(courier);
        CourierCreate.compareCode400Response(response);
    }
}

