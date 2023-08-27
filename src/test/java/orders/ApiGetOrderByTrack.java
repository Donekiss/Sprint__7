package orders;

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
import static org.junit.Assert.assertEquals;

public class ApiGetOrderByTrack {
    private OrderClient orderClient = new OrderClient();
    private static Order order;
    private int track;

    @Before
    public  void setUp(){
        RestAssured.baseURI = BaseUrl.getBASE_URL();
        order = OrderGenerator.randomOrder();
        Response responseOrder = orderClient.create(order);
        track = responseOrder.jsonPath().getInt("track");
    }
    @Test
    @DisplayName("Check status code /api/v1/orders/track")
    public void testGetOrderByTrack() {
        Response responseGetOrderByTrack = orderClient.getOrderByTrack(track);
        assertEquals("Получение существующего заказа по номеру", HttpStatus.SC_OK, responseGetOrderByTrack.statusCode());
    }
    @Test
    @DisplayName("Check status code and body response with bad data /api/v1/orders/track")
    public void testGetOrderByTrackWithOutNumber() {
        Response responseGetOrderByTrack = given()
                .contentType("application/json")
                .queryParam("t")
                .when()
                .get("api/v1/orders/track");
        assertEquals("Получение заказа без номера", HttpStatus.SC_BAD_REQUEST, responseGetOrderByTrack.statusCode());

        JSONObject jsonResponse = new JSONObject(responseGetOrderByTrack.getBody().asString());
        String actualMessage = jsonResponse.getString("message");
        assertEquals("Неверное сообщение об ошибке", "Недостаточно данных для поиска", actualMessage);
    }
    @Test
    @DisplayName("Check status code and body response with bad data /api/v1/orders/track")
    public void testGetOrderByBadTrack() {
        Response responseGetOrderByTrack = orderClient.getOrderByTrack(0);
        assertEquals("Получение заказа с несуществующим номером", HttpStatus.SC_NOT_FOUND, responseGetOrderByTrack.statusCode());

        JSONObject jsonResponse = new JSONObject(responseGetOrderByTrack.getBody().asString());
        String actualMessage = jsonResponse.getString("message");
        assertEquals("Неверное сообщение об ошибке", "Заказ не найден", actualMessage);
    }
    @After
    public void clearCourier(){
        orderClient.cancel(track);
    }
}
