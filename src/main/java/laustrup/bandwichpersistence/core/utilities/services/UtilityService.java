package laustrup.bandwichpersistence.core.utilities.services;

import laustrup.bandwichpersistence.core.utilities.Coollection;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UtilityService {

    /**
     * Creates a Set object of collection, where collection can be null.
     * @param coollection A collection of data that should be transferred to the new Set.
     * @return The Set of the data from the collection.
     * @param <E> The element of the data.
     */
    public static <E> Set<E> toSet(Coollection<E> coollection) {
        Coollection<E> es = Optional.ofNullable(coollection).orElse(new Seszt<>());

        return es.isEmpty()
                ? new HashSet<>()
                : es.toSet();
    }

    /**
     * Creates a Set object of collection, where collection can be null.
     * @param coollection A collection of data that should be transferred to the new Set.
     * @param conversion The way each data is converted.
     * @return The Set of the data from the collection and with the data being converted.
     * @param <M> The type of the data before conversion.
     * @param <E> The type of the data after conversion.
     */
    public static <M, E> Set<E> toSet(Coollection<M> coollection, Function<M, E> conversion) {
        return conversion != null
                ? Arrays.stream(Optional.ofNullable(coollection).orElse(new Seszt<>()).get_data())
                        .map(conversion)
                        .collect(Collectors.toSet())
                : new HashSet<>();
    }
}
