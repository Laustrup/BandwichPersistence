package laustrup.bandwichpersistence.core.services.collections;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapService {

    public static <KEY, VALUE> Map<KEY, VALUE> collectMap(Stream<Map.Entry<KEY, VALUE>> stream) {
        return stream.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
