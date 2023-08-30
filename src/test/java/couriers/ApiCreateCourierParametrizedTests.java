package couriers;

import base.url.BaseUrl;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static utils.Utils.randomString;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ApiCreateCourierParametrizedTests {
    private String requestBody;
    private static String randomValue = randomString(6, 12);

    public ApiCreateCourierParametrizedTests(String requestBody) {
        this.requestBody = requestBody;
    }

    @Before
    public  void setUp(){
        RestAssured.baseURI = BaseUrl.getBASE_URL();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testBadData() {
        return Arrays.asList(new Object[][]{
                {"{\"login\": \"" + randomValue + "\", \"password\": \"" + randomValue + "\"}"},
                {"{\"password\": \"" + randomValue + "\", \"firstName\": \"" + randomValue + "\"}"},
                {"{\"login\": \"" + randomValue + "\", \"firstName\": \"" + randomValue + "\"}"},
                {"{\"login\": \"" + randomValue + "\"}"},
                {"{\"password\": \"" + randomValue + "\"}"},
                {"{\"firstName\": \"" + randomValue + "\"}"}
        });
    }

    @Test
    @DisplayName("Check status code with bad data /api/v1/courier")
    public void testCreateCourierBadRequest() {
        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/courier");
        assertEquals("Недостаточно данных для создания учетной записи", HttpStatus.SC_BAD_REQUEST, response.statusCode());

    }

}
