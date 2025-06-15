package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.items.TestItems.*;
import laustrup.bandwichpersistence.core.models.Model;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static laustrup.bandwichpersistence.core.services.ModelService.*;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

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
            String toString = defineToString(
                    "Test",
                    first,
                    second,
                    new String[]{"first", "second"},
                    new String[]{first.toString(), second.toString()}
            );

            List<UUID> actual = act(() -> getIds(toString).toList());

            for (int i = 0; i < expectations.size(); i++)
                asserting(actual.get(i))
                        .isEqualTo(expectations.get(i));
        });
    }

    @Test
    void canBeEqual() {
        test(() -> {
            Instance instance = Instance.initialise();
            Instances instances = arrange(new Instances(instance, instance));
            Model
                    first = Instance.toModel(instances.expected()),
                    second = Instance.toModel(instances.actual());

            boolean actual = act(() -> ModelService.equals(first, second));

            asserting(actual)
                .isTrue();
        });
    }
}