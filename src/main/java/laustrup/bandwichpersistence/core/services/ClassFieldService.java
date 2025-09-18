package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.utilities.Coollection;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.util.Collection;

public class ClassFieldService {

    public static Member getDeclared(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException exception) {
            throw new RuntimeException(String.format("Could not find field %s with class of %s!\n%s",
                    fieldName,
                    clazz.getSimpleName(),
                    exception.getMessage()
            ));
        }
    }

    public static boolean memberIsCollection(Member member) {
        return new Seszt<>(Collection.class, Coollection.class, Array.class).stream()
                .anyMatch(collection -> collection.isAssignableFrom(member.getDeclaringClass()));
    }

    public static boolean memberIsPartOfEntity(Member member) {
        return member.getDeclaringClass().getDeclaredFields().length > 0;
    }
}
