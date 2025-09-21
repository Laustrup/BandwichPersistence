package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.persistence.models.members.InheritanceField;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import laustrup.bandwichpersistence.core.utilities.Coollection;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static laustrup.bandwichpersistence.core.persistence.services.DatabaseColumnService.fieldIsEqualToColumn;

public class ClassFieldService {

    public static Member getDeclared(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException exception) {
            Member member = getDeclaredFromSuperClass(clazz, fieldName);

            if (member == null)
                throw new RuntimeException(String.format(
                        "Could not find field %s with class of %s!",
                        fieldName,
                        clazz.getSimpleName()
                ), exception);
            else
                return member;
        }
    }

    private static InheritanceField getDeclaredFromSuperClass(Class<?> clazz, String fieldName) {
        if (clazz == Object.class)
            return null;

        Class<?> superClass = clazz.getSuperclass();
        InheritanceField member = null;

        while (superClass != null && (member != null || !superClass.equals(Object.class))) {
            try {
                member = new InheritanceField(clazz, superClass.getDeclaredField(fieldName));
            } catch (NoSuchFieldException ignored) {}

            superClass = superClass.getSuperclass();
        }

        return member;
    }

    public static boolean memberIsCollection(Member member) {
        return new Seszt<>(Collection.class, Coollection.class, Array.class).stream()
                .anyMatch(collection -> collection.isAssignableFrom(member.getDeclaringClass()));
    }

    public static boolean memberIsPartOfEntity(Member member) {
        return member.getDeclaringClass().getDeclaredFields().length > 0;
    }

    public static Optional<Field> getField(Class<?> clazz, Table.Column column) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> fieldIsEqualToColumn(field, column))
                .findFirst();
    }
}
