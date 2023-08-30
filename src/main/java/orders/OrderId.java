package orders;

import io.qameta.allure.internal.shadowed.jackson.databind.JsonNode;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

public class OrderId {

    ObjectMapper objectMapper = new ObjectMapper();
    private final OrderClient orderClient = new OrderClient();
    private int id=0;

    public int getOrderId(){
        Response responseGetList = orderClient.getOrders("?limit=1&page=0");
        try {
            JsonNode root = objectMapper.readTree(responseGetList.body().asString());
            JsonNode ordersArray = root.get("orders");
            JsonNode firstOrder = ordersArray.get(0);
            id = firstOrder.get("id").asInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
