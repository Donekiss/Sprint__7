package couriers;

import base.url.BaseUrl;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.Utils;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class ApiLoginCourierParametrizedTests {
    private static Courier courier;
    private static String valid_login;
    private static String valid_password;
    private final String login;
    private final String password;
    private final int expectedErrorCode;
    private final String expectedMessage;
    private static final CourierClient courierClient = new CourierClient();
    private static String randomValue = Utils.randomString(6, 10);
    private int id;

    public ApiLoginCourierParametrizedTests(String login, String password, int expectedErrorCode, String expectedMessage) {
        this.login = login;
        this.password = password;
        this.expectedErrorCode = expectedErrorCode;
        this.expectedMessage = expectedMessage;
    }
    @BeforeClass
    public static void setUpClass() {
        RestAssured.baseURI = BaseUrl.getBASE_URL();
    }

    @Before
    public void setUp() {
        courier = CourierGenerator.randomCourier();
        courierClient.create(courier);
        Response response = courierClient.login(CourierPass.passFrom(courier));
        id = response.path("id");

        this.valid_login = courier.getLogin();
        this.valid_password = courier.getPassword();
    }
    @Parameterized.Parameters
    public static Collection<Object[]> testBadData() {
        return Arrays.asList(new Object[][]{
                {valid_login, randomValue, HttpStatus.SC_NOT_FOUND, "Учетная запись не найдена"},
                {randomValue, valid_password, HttpStatus.SC_NOT_FOUND, "Учетная запись не найдена"},
                {valid_login, "", HttpStatus.SC_BAD_REQUEST, "Недостаточно данных для входа"},
                {"", valid_password, HttpStatus.SC_BAD_REQUEST,  "Недостаточно данных для входа"},
                {"", "", HttpStatus.SC_BAD_REQUEST, "Недостаточно данных для входа"}
        });
    }
    @Test
    @DisplayName("Check status code with bad data /api/v1/courier")
    public void testLoginStatusCodeWithBadData() {
        CourierPass courierPass = new CourierPass(login, password);

        Response response = courierClient.login(courierPass);

        assertEquals("Неверный статус код", expectedErrorCode, response.statusCode());
    }
    @Test
    @DisplayName("Check message with bad data /api/v1/courier")
    public void testLoginErrorMassageWithBadData() {
        CourierPass courierPass = new CourierPass(login, password);

        Response response = courierClient.login(courierPass);

        if (expectedErrorCode == response.statusCode()) {
            JSONObject jsonResponse = new JSONObject(response.getBody().asString());
            assertTrue(jsonResponse.has("message"));

            String actualMessage = jsonResponse.getString("message");
            assertEquals("Неверное сообщение об ошибке", expectedMessage, actualMessage);
        } else  {
            fail("Проверка сообщения об ошибке не проведена из-за несовпадения статус кода");
        }
    }
    @After
    public void clearCourier(){
        courierClient.delete(id);
    }
}

