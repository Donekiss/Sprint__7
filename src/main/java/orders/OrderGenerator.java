package orders;

import utils.Utils;

public class OrderGenerator {
    public static Order randomOrder(){
        return new Order()
                .withFirstName(Utils.randomString(6))
                .withLastName(Utils.randomString(6))
                .withAddress(Utils.randomString(16))
                .withMetroStation(Utils.randomInteger(1))
                .withPhone(Utils.randomInteger(11))
                .withRentTime(Utils.randomNumber(1))
                .withDeliveryDate("2023-06-06")
                .withComment(Utils.randomString(20))
                .withColor(new String[]{"BLACK"});
    }
}
