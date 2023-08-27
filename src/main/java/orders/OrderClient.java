package orders;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String CREATE_ORDER = "/api/v1/orders";
    private static final String GET_ORDERS = "/api/v1/orders";
    private static final String ACCEPT_ORDER = "/api/v1/orders/accept/:id";
    private static final String GET_ORDER_BY_TRACK = "/api/v1/orders/track";
    public Response create(Order order){
        return given()
                .contentType("application/json")
                .body(order)
                .when()
                .post(CREATE_ORDER);
    }
    public Response getOrders(String order){
        return given()
                .contentType("application/json")
                .when()
                .get(GET_ORDERS + order);
    }
    public Response getOrderByTrack(int track){
        return given()
                .contentType("application/json")
                .queryParam("t", track)
                .when()
                .get(GET_ORDER_BY_TRACK);
    }
    public Response accept (int id, int courierId){
        return given()
                .contentType("application/json")
                .queryParam("courierId", courierId)
                .when()
                .put(ACCEPT_ORDER.replace(":id",String.valueOf(id)));
    }
    public Response cancel (int  cancel){
        return given()
                .contentType("application/json")
                .queryParam("track", cancel)
                .when()
                .put("/api/v1/orders/cancel");
    }
}
