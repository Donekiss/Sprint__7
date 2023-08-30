package orders;

import base.url.BaseUrl;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ApiCreateOrderTests {
    private OrderClient orderClient = new OrderClient();
    private JSONObject requestBody;
    private int track;

    public ApiCreateOrderTests(JSONObject requestBody) {this.requestBody = requestBody;}
    @Before
    public  void setUp(){
        RestAssured.baseURI = BaseUrl.getBASE_URL();
    }
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        JSONObject attributes = new JSONObject()
                .put("firstName", "Naruto")
                .put("lastName", "Uchiha")
                .put("address", "Konoha, 142 apt.")
                .put("metroStation", "4")
                .put("phone", "+7 800 355 35 35")
                .put("rentTime", 5)
                .put("deliveryDate", "2020-06-06")
                .put("comment", "Saske, come back to Konoha");
        return Arrays.asList(new Object[][]{
                {attributes.put("color", new JSONArray().put("BLACK").put("GRAY"))},
                {attributes.put("color", new JSONArray().put("BLACK"))},
                {attributes.put("color", new JSONArray().put("GRAY"))},
                {attributes.put("color", new JSONArray())}
        });
    }
    @Test
    @DisplayName("Check status code and response body /api/v1/orders")
    public void testCreateOrder() {
        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/orders");

        assertEquals("Создание заказов", HttpStatus.SC_CREATED, response.statusCode());
        JSONObject jsonResponse = new JSONObject(response.getBody().asString());
        assertTrue(jsonResponse.has("track"));
        track = response.jsonPath().getInt("track");
    }
    @After
    public void clearCourier(){

        orderClient.cancel(track);
    }
}
