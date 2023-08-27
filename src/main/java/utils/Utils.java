package utils;

import java.util.Random;

public class Utils {
    public static String randomString(int minLength, int maxLength) {
        Random random = new Random();
        int leftLimit = 97; // код a
        int rightLimit = 122; // код z

        int randomLength = minLength + random.nextInt(maxLength - minLength + 1);
        StringBuilder buffer = new StringBuilder(randomLength);

        for (int i = 0; i < randomLength; i++) {
            int randomLimitedInt = leftLimit + random.nextInt(rightLimit - leftLimit + 1);
            buffer.append(Character.toChars(randomLimitedInt));
        }
        return buffer.toString();
    }

    public static String randomString(int length) {
        return randomString(length, length);
    }

    public static String randomInteger(int minLength, int maxLength) {
        Random random = new Random();
        int leftLimit = 49; // код 1
        int rightLimit = 57; // код 9

        int randomLength = minLength + random.nextInt(maxLength - minLength + 1);
        StringBuilder buffer = new StringBuilder(randomLength);

        for (int i = 0; i < randomLength; i++) {
            int randomLimitedInt = leftLimit + random.nextInt(rightLimit - leftLimit + 1);
            buffer.append(Character.toChars(randomLimitedInt));
        }
        return buffer.toString();
    }
    public static String randomInteger(int length) {
        return randomString(length, length);
    }

    public static int randomNumber(int length) {
        Random random = new Random();
        int leftLimit = 49; // код 1
        int rightLimit = 57; // код 9
        int buffer=0;

        for (int i = 0; i < length; i++) {
            buffer = leftLimit + random.nextInt(rightLimit - leftLimit + 1);
        }
        return buffer;
    }
}
