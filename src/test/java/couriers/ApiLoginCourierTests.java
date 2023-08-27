package couriers;

import base.url.BaseUrl;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApiLoginCourierTests {
    private final CourierClient courierClient = new CourierClient();
    private int id;
    private static Courier courier;
    @Before
    public  void setUp(){
        RestAssured.baseURI = BaseUrl.getBASE_URL();
        courier = CourierGenerator.randomCourier();
        courierClient.create(courier);
    }

    @Test
    @DisplayName("Check status code /api/v1/courier/login")
    public void testLoginCourier() {
        CourierPass courierPass = CourierPass.passFrom(courier);
        Response response = courierClient.login(courierPass);
        id = response.path("id");

        assertEquals("Курьер не залогинился", HttpStatus.SC_OK, response.statusCode());
    }
    @Test
    @DisplayName("Check response /api/v1/courier/login")
    public void testLoginCourierResponseId() {
        CourierPass courierPass = CourierPass.passFrom(courier);
        Response response = courierClient.login(courierPass);

        JSONObject jsonResponse = new JSONObject(response.getBody().asString());
        assertTrue(jsonResponse.has("id"));

        id = response.path("id");
    }
    @Test
    @DisplayName("Check status code and response with bad data /api/v1/courier/login")
    public void testLoginNotExistingCourier() {
        CourierPass courierPass = CourierPass.passFrom(courier);
        Response response = courierClient.login(courierPass);
        id = response.path("id");
        Response deleteResponse = courierClient.delete(id);

        assertTrue("Удаление курьера", deleteResponse.statusCode() == HttpStatus.SC_OK);

        CourierPass courierPass2 = CourierPass.passFrom(courier);

        Response response2 = courierClient.login(courierPass2);

        assertEquals("Попытка входа несуществующего (удаленного) курьера", HttpStatus.SC_NOT_FOUND, response2.statusCode());

        assertEquals("Сообщения об отсутсвии курьера в БД","Учетная запись не найдена", response2.path("message"));
    }
    @After
    public void clearCourier(){

        courierClient.delete(id);
    }
}

