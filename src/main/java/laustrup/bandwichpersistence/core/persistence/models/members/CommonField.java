package laustrup.bandwichpersistence.core.persistence.models.members;

import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.DatabaseEntity;
import lombok.Getter;

import java.lang.reflect.Field;

public class CommonField extends SimpleField {

    @Getter
    protected final Field _reflection;

    public CommonField(Class<?> declaringClass, Field field) {
        this(declaringClass, field.getName(), field);
    }

    public CommonField(Class<?> declaringClass, String name, Field field) {
        super(declaringClass, name);
        _reflection = field;
    }

    public CommonField(Class<?> declaringClass, DatabaseEntity.Column column, Field field) {
        this(declaringClass, column.value(), field);
    }

    public CommonField(Class<?> clazz, DatabaseField databaseField, Field field) {
        this(clazz, databaseField.column().title(), field);
    }
    
    @Override
    public int getModifiers() {
        return _reflection.getModifiers();
    }
}
