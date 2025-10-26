package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.identification.Identity;
import laustrup.bandwichpersistence.core.models.identification.Signature;
import laustrup.bandwichpersistence.items.TestItems.Instance;
import laustrup.bandwichpersistence.items.TestItems.Instances;
import org.junit.jupiter.api.Test;

import java.util.List;

import static laustrup.bandwichpersistence.core.services.ModelService.defineToString;
import static laustrup.bandwichpersistence.core.services.ModelService.getIds;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

class ModelServiceTests extends BandwichTester {

  @Test
  void canGetIdsFromToString() {
    test(() -> {
      List<Instance.Id> expectations = arrange(() -> List.of(
          Instance.Id.randomize(),
          Instance.Id.randomize()
      ));
      Instance.Id
          first = expectations.getFirst(),
          second = expectations.getLast();
      String toString = defineToString(
          "Test",
          first,
          second,
          new String[]{"first", "second"},
          new String[]{first.toString(), second.toString()}
      );

      List<Instance.Id> actual = act(() -> getIds(toString)
          .map(id -> new Instance.Id(id.get_value()))
          .toList()
      );

      for (int i = 0; i < expectations.size(); i++)
        asserting(actual.get(i))
            .is(expectations.get(i));
    });
  }

  @Test
  void canBeEqual() {
    test(() -> {
      Instance instance = Instance.initialise();
      Instances instances = arrange(new Instances(instance, instance));
      Model<Identity<Signature.UUID>, Signature.UUID>
          first = Instance.toModel(instances.expected()),
          second = Instance.toModel(instances.actual());

      boolean actual = act(() -> ModelService.equals(first, second));

      asserting(actual)
          .isTrue();
    });
  }
}