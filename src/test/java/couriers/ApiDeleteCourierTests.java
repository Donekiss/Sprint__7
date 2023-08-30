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

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class ApiDeleteCourierTests {
    private final CourierClient courierClient = new CourierClient();
    private int id;
    private static Courier courier;
    @Before
    public  void setUp(){
        RestAssured.baseURI = BaseUrl.getBASE_URL();
        courier = CourierGenerator.randomCourier();
        courierClient.create(courier);

        Response response = courierClient.login(CourierPass.passFrom(courier));
        id = response.path("id");
    }
    @Test
    @DisplayName("Check status code /api/v1/courier/:id")
    public void testDeleteCourierCode() {
        Response deleteResponse = courierClient.delete(id);
        assertEquals("Удаление курьера", HttpStatus.SC_OK, deleteResponse.statusCode());
    }
    @Test
    @DisplayName("Check response body /api/v1/courier/:id")
    public void testDeleteCourierBody() {
        Response deleteResponse = courierClient.delete(id);

        JSONObject jsonResponse = new JSONObject(deleteResponse.getBody().asString());
        assertTrue(jsonResponse.has("ok") && jsonResponse.getBoolean("ok"));
    }
    @Test
    @DisplayName("Check status code and response body Bad Data /api/v1/courier/:id")
    public void testDeleteCourierWithOutId() {
        Response deleteResponse = given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/:id");

        assertEquals("Удаление курьера без id", HttpStatus.SC_BAD_REQUEST, deleteResponse.statusCode());
        if (HttpStatus.SC_BAD_REQUEST == deleteResponse.statusCode()) {
            JSONObject jsonResponse = new JSONObject(deleteResponse.getBody().asString());
            assertTrue(jsonResponse.has("message"));

            String actualMessage = jsonResponse.getString("message");
            assertEquals("Неверное сообщение об ошибке", "Недостаточно данных для удаления курьера", actualMessage);
        } else {
            fail("Проверка сообщения об ошибке не проведена из-за несовпадения статус кода");
        }
    }
    @Test
    @DisplayName("Check status code and response body Bad Data /api/v1/courier/:id")
    public void testDeleteNotExistingCourier() {
        Response deleteResponse = courierClient.delete(id + 222);

        assertEquals("Удаление не существующего курьера", HttpStatus.SC_NOT_FOUND, deleteResponse.statusCode());
        JSONObject jsonResponse = new JSONObject(deleteResponse.getBody().asString());
        assertTrue(jsonResponse.has("message"));

        String actualMessage = jsonResponse.getString("message");
        assertEquals("Неверное сообщение об ошибке", "Курьера с таким id нет", actualMessage);
    }
    @After
    public void clearCourier(){

        courierClient.delete(id);
    }
}
