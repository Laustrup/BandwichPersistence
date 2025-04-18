package laustrup.bandwichpersistence.core.services;

import java.util.Objects;

public class ConvertingService {

    public static int of(Integer integer) {
        return Objects.requireNonNullElse(integer, 0);
    }

    public static long of(Long value) {
        return Objects.requireNonNullElse(value, 0L);
    }

    public static boolean of(Boolean value) {
        return Objects.requireNonNullElse(value, false);
    }
}
