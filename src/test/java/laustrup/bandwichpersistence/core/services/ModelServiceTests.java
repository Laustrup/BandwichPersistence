package laustrup.bandwichpersistence.core.services;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ModelServiceTests {

    @Test
    void canGetIdsFromToString() {
        //ARRANGE
        List<UUID> expectations = List.of(
                UUID.randomUUID(),
                UUID.randomUUID()
        );
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

        //ACT
        List<UUID> act = ModelService.getIds(toString).toList();

        //ASSERT
        for (int i = 0; i < expectations.size(); i++)
            assertEquals(expectations.get(i), act.get(i));
    }
}