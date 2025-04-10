package laustrup.bandwichpersistence.core.services;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ModelServiceTest {

    @Test
    void canGetIdsFromToString() {
        //ARRANGE
        UUID
                first = UUID.randomUUID(),
                second = UUID.randomUUID();
        String toString = String.format(
                "test(first_id%s,%ssecond_id%s)",
                ModelService.get_toStringKeyValueSplitter() + first,
                ModelService.get_toStringFieldSplitter(),
                ModelService.get_toStringKeyValueSplitter() + second
        );

        //ACT
        List<UUID> act = ModelService.getIds(toString).toList();

        //ASSERT
        for (UUID id : act)
            assertTrue(id.toString().equals(first) || id.toString().equals(second));
    }
}