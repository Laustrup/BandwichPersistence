package laustrup.bandwichpersistence.core.utilities;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeToken<TYPE> {
    private Type _type;

    protected TypeToken() {
        Type superClass = getClass().getGenericSuperclass();
        _type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return _type;
    }
}
