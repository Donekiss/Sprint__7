package orders;

import base.url.BaseUrl;
import couriers.Courier;
import couriers.CourierClient;
import couriers.CourierGenerator;
import couriers.CourierPass;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApiOrderAcceptTest {
    private final CourierClient courierClient = new CourierClient();
    private final OrderId orderId = new OrderId();
    private final OrderClient orderClient = new OrderClient();
    private int idCouriers;
    private int idOrders;
    private static Courier courier;

    @Before
    public  void setUp(){
        RestAssured.baseURI = BaseUrl.getBASE_URL();
        courier = CourierGenerator.randomCourier();
        courierClient.create(courier);
        Response responseClient = courierClient.login(CourierPass.passFrom(courier));
        idCouriers = responseClient.path("id");

        idOrders = orderId.getOrderId();
    }
    @Test
    @DisplayName("Check status code and body -Ok- /api/v1/orders/accept/:id")
    public void testOrderAccept() {
        Response responseOrderAccept = orderClient.accept(idOrders, idCouriers);
        assertEquals("Принятие заказа", HttpStatus.SC_OK, responseOrderAccept.statusCode());

        JSONObject jsonResponse = new JSONObject(responseOrderAccept.getBody().asString());
        assertTrue(jsonResponse.has("ok") && jsonResponse.getBoolean("ok"));
    }
    @Test
    @DisplayName("Check status code and body -Ok- /api/v1/orders/accept/:id")
    public void testOrderAcceptWithOutIdCourier() {
        Response responseOrderAccept = given()
                .contentType("application/json")
                .queryParam("courierId")
                .when()
                .put("/api/v1/orders/accept/" + idOrders);

        assertEquals("Принятие заказа без Id курьера", HttpStatus.SC_BAD_REQUEST, responseOrderAccept.statusCode());

        JSONObject jsonResponse = new JSONObject(responseOrderAccept.getBody().asString());
        String actualMessage = jsonResponse.getString("message");
        assertEquals("Неверное сообщение об ошибке", "Недостаточно данных для поиска", actualMessage);
    }
    @Test
    @DisplayName("Check status code and body -BAD_REQUEST- /api/v1/orders/accept/:id")
    public void testOrderAcceptWithOutIdOrder() {
        System.out.println("id" + idCouriers);
        System.out.println("idOrders" + idOrders);

        Response responseOrderAccept = given()
                .contentType("application/json")
                .queryParam("courierId", idCouriers)
                .when()
                .put("/api/v1/orders/accept/:id");
        assertEquals("Принятие заказа без Id заказа", HttpStatus.SC_BAD_REQUEST, responseOrderAccept.statusCode());

        JSONObject jsonResponse = new JSONObject(responseOrderAccept.getBody().asString());
        String actualMessage = jsonResponse.getString("message");
        assertEquals("Неверное сообщение об ошибке", "Недостаточно данных для поиска", actualMessage);
    }
    @Test
    @DisplayName("Check status code and body -NOT_FOUND- /api/v1/orders/accept/:id")
    public void testOrderAcceptWithBadIdCourier() {
        Response responseOrderAccept = orderClient.accept(idOrders, idCouriers+10000);
        assertEquals("Принятие заказа не несуществующего курьера", HttpStatus.SC_NOT_FOUND, responseOrderAccept.statusCode());

        JSONObject jsonResponse = new JSONObject(responseOrderAccept.getBody().asString());
        String actualMessage = jsonResponse.getString("message");
        assertEquals("Неверное сообщение об ошибке", "Курьера с таким id не существует", actualMessage);
    }
    @Test
    @DisplayName("Check status code and body -NOT_FOUND- /api/v1/orders/accept/:id")
    public void testOrderAcceptWithBadIdOrder() {
        Response responseOrderAccept = orderClient.accept(0, idCouriers);
        assertEquals("Принятие не несуществующего заказа", HttpStatus.SC_NOT_FOUND, responseOrderAccept.statusCode());

        JSONObject jsonResponse = new JSONObject(responseOrderAccept.getBody().asString());
        String actualMessage = jsonResponse.getString("message");
        assertEquals("Неверное сообщение об ошибке", "Заказа с таким id не существует", actualMessage);
    }

    @After
    public void clearCourier(){
        courierClient.delete(idCouriers);
    }
}
