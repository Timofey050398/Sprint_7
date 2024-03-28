import bodyclasses.Constants;
import bodyclasses.request.CourierCreate;
import bodyclasses.request.CourierDelete;
import bodyclasses.request.CourierLogin;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static java.lang.String.valueOf;
import static org.hamcrest.CoreMatchers.equalTo;

public class CourierDeleteTest {
    Random random = new Random();
    String login = valueOf(random.nextInt(1000000));
    String password = valueOf(random.nextInt(10000));
    String firstName = "Andrey";
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
    public void SuccessResponseOkTrue(){
        String id = valueOf(CourierLogin.getId(login,password));
        Response response = CourierDelete.sendDeleteCourier(id);
        response.then()
                .assertThat()
                .body("ok", equalTo(true));
    }
    @Test
    public void CorrectResponseWhenIdNotFound(){
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
    public void CorrectResponseWhenIdIsEmpty(){
        String id = "";
        Response response = CourierDelete.sendDeleteCourier(id);
        response.then()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для удаления курьера"));
    }
}

