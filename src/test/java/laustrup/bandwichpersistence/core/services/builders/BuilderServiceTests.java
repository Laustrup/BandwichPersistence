package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.TestItems.Instance;
import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.ResultSet;
import java.util.function.Function;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.TestItems.*;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

class BuilderServiceTests extends BandwichTester {

    private final BuilderService<Instance> _builderService = new BuilderService<>(
            Instance.class,
            Logger.getLogger(BuilderServiceTests.class.getSimpleName())
    ) {
        @Override
        protected void completion(Instance reference, Instance object) {

        }

        @Override
        protected Function<Function<String, Field>, Instance> logic(ResultSet resultSet) {
            return null;
        }
    };

    @ParameterizedTest
    @CsvSource(value = {"true", "false"})
    void canCombine(boolean shouldUpdate) {
        test(() -> {
            Instance entity = Instance.initialise();
            Seszt<Instance> collection = new Seszt<>(
                    Instance.initialise(),
                    Instance.initialise(shouldUpdate ? entity : null)
            );
            InstanceCollection arrangement = arrange(new InstanceCollection(collection, entity));

            act(() -> _builderService.combine(arrangement.collection(), arrangement.entity()));

            asserting(arrangement.collection())
                    .anyMatches(instance -> ((Instance) instance).get_id().equals(entity.get_id()))
                    .inCase(
                            shouldUpdate,
                            instances -> instances.stream()
                                    .noneMatch(instance -> instance.isSameAs(entity))
                    );
        });
    }
}