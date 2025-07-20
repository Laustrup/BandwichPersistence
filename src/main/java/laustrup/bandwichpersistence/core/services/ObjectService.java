package laustrup.bandwichpersistence.core.services;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

/** A Service for standard Object algorithms. */
public class ObjectService extends Service {

    /**
     * Checks if the object is null or not.
     * If it is null, it will not apply the function.
     * @param input The Object that can possibly be null.
     * @param function A function that should only be applied, if the input isn't null.
     * @return The return of the function if the input isn't null, otherwise it returns null.
     */
    public static <E, M> M ifExists(E input, Function<E, M> function) {
        return ifExists(Optional.ofNullable(input), function);
    }

    /**
     * Checks if the object is null or not.
     * If it is null, it will not run the runnable.
     * @param input The Object that can possibly be null.
     * @param runnable A runnable that should only be run, if the input isn't null.
     */
    public static void ifExists(Object input, Runnable runnable) {
        if (input != null)
            runnable.run();
    }

    public static <E, M> M ifExists(Optional<E> input, Function<E, M> function) {
        return input.map(function).orElse(null);
    }

    public static <E> E ifTrue(boolean statement, E target, E alternative) {
        return statement ? target : alternative;
    }

    @SuppressWarnings("unchecked")
    public static <VALUE> VALUE getFieldValue(Object object, String fieldName) {
        Field declaredField = getDeclaredField(object, fieldName);
        Object fieldValue = null;

        try {
            fieldValue = declaredField.get(object);
        } catch (IllegalAccessException e) {
            fieldValue = getUnaccessibleValue(object, declaredField);
        }

        return (VALUE) fieldValue;
    }

    private static Object getUnaccessibleValue(Object object, Field field) {
        Object value = null;
        field.setAccessible(true);
        try {
            value = field.get(object);
        } catch (IllegalAccessException ignore) {}
        field.setAccessible(false);

        return value;
    }

    public static Field getDeclaredField(Object object, String fieldName) {
        return Arrays.stream(object.getClass().getDeclaredFields())
                .filter(field -> field.getName().equals(fieldName))
                .findFirst()
                .orElse(null);
    }
}
