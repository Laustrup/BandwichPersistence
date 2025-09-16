package laustrup.bandwichpersistence.core.persistence.models;

import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.models.annotations.DatabaseEntity;

import java.lang.reflect.Member;

public class CommonMember implements Member {

    private final Class<?> _declaringClass;
    private final String _name;

    public CommonMember(Class<?> declaringClass, String name) {
        _declaringClass = declaringClass;
        _name = name;
    }

    public CommonMember(Class<?> declaringClass, DatabaseEntity.Column column) {
        _declaringClass = declaringClass;
        _name = column.title();
    }

    public CommonMember(Class<?> clazz, DatabaseField field) {
        _declaringClass = clazz;
        _name = field.column().title();
    }

    @Override
    public Class<?> getDeclaringClass() {
        return _declaringClass;
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
