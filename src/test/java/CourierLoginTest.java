import bodyclasses.Constants;
import bodyclasses.request.CourierCreate;
import bodyclasses.request.CourierDelete;
import bodyclasses.request.CourierLogin;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description; // импорт Description
import java.util.HashMap;


public class CourierLoginTest {
    Faker faker = new Faker();
    String login = faker.name().username();
    String password = faker.internet().password();
    String firstName = faker.name().firstName();
    HashMap<String, Object> requestBody = new HashMap<>();

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
        CourierCreate courier = new CourierCreate(login,password,firstName);
        CourierCreate.sendPostCourierCreate(courier);
    }

    @After
    @Description("Delete Courier")
    public void deleteCourier() {
        Integer _id = CourierLogin.getId(login, password);
        if (_id != null) {
            String id = String.valueOf(_id);
            CourierDelete.sendDeleteCourier(id);
        }
    }

    @Test
    @DisplayName("Courier can authorize")
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void canLoginCourier() {
        CourierLogin courier = new CourierLogin(login,password);
        Response response = CourierLogin.sendPostCourierLogin(courier);
        CourierLogin.compareSuccessResponse(response);
    }
    @Test
    @DisplayName("Код ответа 400, если в запросе не передан параметр login")
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void cantLoginWithoutLoginParam() {
        requestBody.put("password",password);
        Response response = CourierLogin.sendPostCourierLogin(requestBody);
        CourierLogin.compareCode400Response(response);
    }
    @Test
    @DisplayName("Код ответа 400, если в запросе не передан параметр password")
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void cantLoginWithoutPasswordParam() {
        requestBody.put("login",login);
        Response response = CourierLogin.sendPostCourierLogin(requestBody);
        CourierLogin.compareCode400Response(response);
    }
    @Test
    @DisplayName("Код ответа 400, если в запросе не передан ключ password")
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void cantLoginWithoutPasswordKey() {
        CourierLogin courier = new CourierLogin(login,"");
        Response response = CourierLogin.sendPostCourierLogin(courier);
        CourierLogin.compareCode400Response(response);
    }
    @Test
    @DisplayName("Код ответа 400, если в запросе не передан ключ login")
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void cantLoginWithoutLoginKey() {
        CourierLogin courier = new CourierLogin("",password);
        Response response = CourierLogin.sendPostCourierLogin(courier);
        CourierLogin.compareCode400Response(response);
    }
    @Test
    @DisplayName("Код ответа 404, если в запросе передан не существующий login")
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void cantLoginWithWrongLoginKey() {
        CourierLogin courier = new CourierLogin(login+password,password);
        Response response = CourierLogin.sendPostCourierLogin(courier);
        CourierLogin.compareIdNotFoundResponse(response);
    }
    @Test
    @DisplayName("Код ответа 404, если в запросе передан не верный password")
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void cantLoginWithWrongPasswordKey() {
        CourierLogin courier = new CourierLogin(login,password+password);
        Response response = CourierLogin.sendPostCourierLogin(courier);
        CourierLogin.compareIdNotFoundResponse(response);
    }
    @Test
    @DisplayName("Код ответа 404, если в запросе передан не верный password и не существующий login")
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void cantLoginWithWrongLoginAndPasswordKeys() {
        CourierLogin courier = new CourierLogin(login+password,password+password);
        Response response = CourierLogin.sendPostCourierLogin(courier);
        CourierLogin.compareIdNotFoundResponse(response);
    }
    @Test
    @DisplayName("Успешный запрос возвращает id")
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void requestWithCode200HasIdAtResponce() {
        CourierLogin courier = new CourierLogin(login,password);
        Response response = CourierLogin.sendPostCourierLogin(courier);
        CourierLogin.compareResponseHasId(response);
    }
}
