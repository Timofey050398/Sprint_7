import bodyclasses.Constants;
import bodyclasses.request.CourierCreate;
import bodyclasses.request.CourierDelete;
import bodyclasses.request.CourierLogin;
import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static java.lang.String.valueOf;
import static org.hamcrest.CoreMatchers.equalTo;

public class CourierDeleteTest {
    Faker faker = new Faker();
    String login = faker.name().username();
    String password = faker.internet().password();
    String firstName = faker.name().firstName();
    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
        CourierCreate courier = new CourierCreate(login,password,firstName);
        CourierCreate.sendPostCourierCreate(courier);
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
    public void successResponseOkTrue(){
        String id = valueOf(CourierLogin.getId(login,password));
        Response response = CourierDelete.sendDeleteCourier(id);
        response.then()
                .assertThat()
                .body("ok", equalTo(true));
    }
    @Test
    public void correctResponseWhenIdNotFound(){
        String id =valueOf(CourierLogin.getId(login,password));
        id = id+"000";
        Response response = CourierDelete.sendDeleteCourier(id);
        response.then()
                .assertThat()
                .statusCode(404)
                .and()
                .body("message", equalTo("Курьера с таким id нет"));
    }
    @Test
    public void correctResponseWhenIdIsEmpty(){
        String id = "";
        Response response = CourierDelete.sendDeleteCourier(id);
        response.then()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для удаления курьера"));
    }
}

