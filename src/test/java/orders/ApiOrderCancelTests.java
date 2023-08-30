package orders;

import base.url.BaseUrl;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ApiOrderCancelTests {
    private OrderClient orderClient = new OrderClient();
    private static Order order;
    private  int idOrders;
    @Before
    public  void setUp(){
        RestAssured.baseURI = BaseUrl.getBASE_URL();
        order = OrderGenerator.randomOrder();
        Response responseOrder = orderClient.create(order);
        idOrders = responseOrder.jsonPath().getInt("track");
    }
    @Test
    @DisplayName("Check status code -Ok- /api/v1/orders/cancel")
    public void testOrderCanceled() {
        Response responseOrderCanceled = orderClient.cancel(idOrders);

        assertEquals("Отмена заказа", HttpStatus.SC_OK, responseOrderCanceled.statusCode());
    }
}
