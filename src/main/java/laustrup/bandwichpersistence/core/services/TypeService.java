package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.utilities.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeService<TYPE> {

    public Type getType() {
        class TypeTokenContainer extends TypeToken<TYPE> {}

        TypeTokenContainer container = new TypeTokenContainer();
        ParameterizedType parameterizedType = (ParameterizedType) container.getType();

        return parameterizedType.getActualTypeArguments()[0];
    }

    public Class<?> getClassOfType() {
        return getType().getClass();
    }
}
