package laustrup.bandwichpersistence.core.services;

import java.lang.reflect.Field;

public class ClassFieldService {

    public static Field getDeclared(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
