package asundukov.multithreading.commons;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    public static final long RANGE = 100000000;

    private Utils() {
    }

    public static Long randomPayload(long payloadSize) {
        return ThreadLocalRandom.current().nextLong(payloadSize);
    }
}
