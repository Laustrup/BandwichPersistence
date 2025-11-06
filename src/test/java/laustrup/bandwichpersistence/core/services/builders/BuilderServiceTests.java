package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import laustrup.bandwichpersistence.items.TestItems;
import laustrup.bandwichpersistence.items.TestItems.Instance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static laustrup.bandwichpersistence.items.TestItems.InstanceCollection;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

class BuilderServiceTests extends BandwichTester {

  private final BuilderService<Instance> _builderService = new BuilderService<>(
      TestItems.EntityDataCollection.get_instance()
  ) {
    @Override
    protected void completion(Instance reference, Instance object) {

    }
  };

  @ParameterizedTest
  @CsvSource(value = {"true", "false"})
  void canCombine(boolean shouldUpdate) {
    mocked(() -> {
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