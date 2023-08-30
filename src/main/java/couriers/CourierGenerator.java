package couriers;

import utils.Utils;

public class CourierGenerator {
    public static Courier randomCourier(){
        return new Courier()
                .withFirstName(Utils.randomString(10))
                .withPassword(Utils.randomString(8))
                .withLogin(Utils.randomString(6));
    }
}
