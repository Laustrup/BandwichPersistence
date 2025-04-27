package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.BandwichTester;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ModelServiceTests extends BandwichTester {

    @Test
    void canGetIdsFromToString() {
        test(() -> {
            List<UUID> expectations = arrange(() -> List.of(
                    UUID.randomUUID(),
                    UUID.randomUUID()
            ));

            UUID
                    first = expectations.getFirst(),
                    second = expectations.getLast();
            String toString = ModelService.defineToString(
                    "Test",
                    first,
                    second,
                    new String[]{"first", "second"},
                    new String[]{first.toString(), second.toString()}
            );

            List<UUID> actual = act(() -> ModelService.getIds(toString).toList());

            for (int i = 0; i < expectations.size(); i++)
                assertEquals(expectations.get(i), actual.get(i));
        });
    }
}