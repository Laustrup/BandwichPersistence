package laustrup.bandwichpersistence.core.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodService {

    public static Method get(Class<?> clazz, String methodName, Object... parameterTypes) {
        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().equals(methodName) && method.getParameterCount() == parameterTypes.length)
                .findAny()
                .orElseThrow();
    }

    @SuppressWarnings("unchecked")
    public static <RETURN> RETURN invoke(Class<?> clazz, String methodName, Object... parameterTypes) {
        try {
            Method method = get(clazz, methodName, parameterTypes);
            boolean isAccessible = method.isAccessible();
            method.setAccessible(true);
            var value = method.invoke(clazz, parameterTypes);
            method.setAccessible(isAccessible);

            return (RETURN) value;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format(
                    "Couldn't invoke method %s of %s",
                    methodName,
                    clazz.getSimpleName()
            ), e);
        } catch (Exception e) {
            throw new RuntimeException(String.format(
                    "Couldn't invoke method %s of %s, properly because of unchecked casting",
                    methodName,
                    clazz.getSimpleName()
            ), e);
        }
    }
}
