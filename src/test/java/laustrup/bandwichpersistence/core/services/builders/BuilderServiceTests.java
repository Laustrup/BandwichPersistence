package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import laustrup.bandwichpersistence.items.TestItems;
import laustrup.bandwichpersistence.items.TestItems.Instance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static laustrup.bandwichpersistence.core.services.ClassFieldService.getField;
import static laustrup.bandwichpersistence.items.TestItems.InstanceCollection;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

class BuilderServiceTests extends BandwichTester {

  private static class TestInstanceBuilder extends BuilderService<Instance> {

    public TestInstanceBuilder() {
      this(null);
    }

    public TestInstanceBuilder(Map<? extends Member, AtomicReference<?>> fields) {
      super(TestItems.EntityDataCollection.get_instance());
      if (fields != null)
        _fields = fields;
    }

    @Override
    protected void completion(Instance reference, Instance object) {

    }
  }

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
      TestInstanceBuilder builder = new TestInstanceBuilder();

      act(() -> builder.combine(arrangement.collection(), arrangement.entity()));

      asserting(arrangement.collection())
          //TODO Make work for should update as well
//          .anyMatches(instance -> ((Instance) instance).get_id().equals(entity.get_id()))
          .inCase(
              shouldUpdate,
              instances -> instances.stream()
                  .noneMatch(instance -> instance.isSameAs(entity))
          );
    });
  }

  @Test
  void canConstruct() {
    test(() -> {
      Instance expected = Instance.initialise();
      TestInstanceBuilder builder = new TestInstanceBuilder(Map.of(
          getField(Instance.class, Instance.Fields._id.name()), new AtomicReference<>(expected.get_id()),
          getField(Instance.class, Instance.Fields._ownerId.name()), new AtomicReference<>(expected.get_ownerId()),
          getField(Instance.class, Instance.Fields._title.name()), new AtomicReference<>(expected.get_title()),
          getField(Instance.class, Instance.Fields._active.name()), new AtomicReference<>(expected.is_active()),
          getField(Instance.class, Instance.Fields._amount.name()), new AtomicReference<>(expected.get_amount())
      ));
      @SuppressWarnings("unchecked")
      Constructor<Instance> constructor = (Constructor<Instance>) arrange(Arrays.stream(Instance.class.getConstructors())
          .filter(instanceConstructor -> instanceConstructor.isAnnotationPresent(Table.Constructor.class))
          .findFirst()
          .orElseThrow()
      );

      Instance actual = act(builder.construct(constructor));

      asserting(actual.toString())
          .is(expected.toString());
    });
  }
}