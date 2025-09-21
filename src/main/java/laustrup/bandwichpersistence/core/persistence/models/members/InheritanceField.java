package laustrup.bandwichpersistence.core.persistence.models.members;

import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.DatabaseEntity;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Member;

@Getter
public class InheritanceField extends CommonField {

    private final Class<?> _entityClass;

    public InheritanceField(Class<?> entityClass, Member member) {
        super(member.getDeclaringClass(), member.getName(), (Field) member);
        _entityClass = entityClass;
    }

    public InheritanceField(Class<?> entityClass, Class<?> declaringClass, String name, Field field) {
        super(declaringClass, name, field);
        _entityClass = entityClass;
    }

    public InheritanceField(Class<?> entityClass, Class<?> declaringClass, DatabaseEntity.Column column, Field field) {
        super(declaringClass, column, field);
        _entityClass = entityClass;
    }

    public InheritanceField(Class<?> entityClass, Class<?> clazz, DatabaseField databaseField, Field field) {
        super(clazz, databaseField, field);
        _entityClass = entityClass;
    }
}
