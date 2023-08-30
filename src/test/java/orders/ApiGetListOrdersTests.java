package orders;

import base.url.BaseUrl;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ApiGetListOrdersTests {
    private OrderClient orderClient = new OrderClient();
    @Before
    public  void setUp(){
        RestAssured.baseURI = BaseUrl.getBASE_URL();
    }
    @Test
    @DisplayName("Check status code /api/v1/orders")
    public void testGetListOrders() {
        Response responseGetList = orderClient.getOrders("");
        assertEquals("Запрос списка заказов", HttpStatus.SC_OK, responseGetList.statusCode());
    }
    @Test
    @DisplayName("Check status code /api/v1/orders?courierId=182539")
    public void testGetListOrdersWithCourierId() {
        Response responseGetList = orderClient.getOrders("?courierId=182539");
        assertEquals("Запрос списка заказов", HttpStatus.SC_OK, responseGetList.statusCode());
    }
    @Test
    @DisplayName("Check status code /api/v1/orders?courierId=182539&limit=1&page=0")
    public void testGetListOrdersWithCourierIdLimitAndPage() {
        Response responseGetList = orderClient.getOrders("?courierId=182539&limit=1&page=0");
        assertEquals("Запрос списка заказов", HttpStatus.SC_OK, responseGetList.statusCode());
    }
}
