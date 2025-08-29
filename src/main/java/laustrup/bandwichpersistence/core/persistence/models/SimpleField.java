package laustrup.bandwichpersistence.core.persistence.models;

import java.lang.reflect.Member;

public class SimpleField implements Member {

    private final String _name;

    public SimpleField(String name) {
        _name = name;
    }

    @Override
    public Class<?> getDeclaringClass() {
        throw new UnsupportedOperationException("A simple field doesn't have any declaring class");
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public int getModifiers() {
        return 0;
    }

    @Override
    public boolean isSynthetic() {
        return false;
    }
}
