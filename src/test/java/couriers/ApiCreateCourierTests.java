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

public class ApiCreateCourierTests {
    private CourierClient courierClient = new CourierClient();
    private int id;
    @Before
    public  void setUp(){
        RestAssured.baseURI = BaseUrl.getBASE_URL();
    }
    @Test
    @DisplayName("Check status code /api/v1/courier/")
    public void testCreateCourier() {

        Courier courier = CourierGenerator.randomCourier();
        Response response = courierClient.create(courier);
        assertEquals("Неверный статус код", HttpStatus.SC_CREATED, response.statusCode());

        Response loginResponse = courierClient.login(CourierPass.passFrom(courier));
        id = loginResponse.path("id");
    }
    @Test
    @DisplayName("Check response body /api/v1/courier")
    public void testCreateCourierWithResponse() {

        Courier courier = CourierGenerator.randomCourier();
        Response response = courierClient.create(courier);

        JSONObject jsonResponse = new JSONObject(response.getBody().asString());
        assertTrue(jsonResponse.has("ok") && jsonResponse.getBoolean("ok"));

        Response loginResponse = courierClient.login(CourierPass.passFrom(courier));
        id = loginResponse.path("id");
    }
    @Test
    @DisplayName("Check status code create duplicate /api/v1/courier")
    public void testCreateCourierDuplicate() {
        Courier courier = CourierGenerator.randomCourier();
        courierClient.create(courier);

        Response responseDuplicate = courierClient.create(courier);

        assertEquals("Создание дубликата курьера", HttpStatus.SC_CONFLICT, responseDuplicate.statusCode());

        Response loginResponse = courierClient.login(CourierPass.passFrom(courier));
        id = loginResponse.path("id");
    }
    @After
    public void clearCourier(){

        courierClient.delete(id);
    }
}
