package couriers;

public class CourierPass {
    private final String login;
    private final String password;

    public CourierPass(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static CourierPass passFrom(Courier courier) {
        return new CourierPass(courier.getLogin(), courier.getPassword());
    }
}
