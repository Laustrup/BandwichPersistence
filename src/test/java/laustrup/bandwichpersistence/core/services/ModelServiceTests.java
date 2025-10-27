package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.identification.Identity;
import laustrup.bandwichpersistence.core.models.identification.Signature;
import laustrup.bandwichpersistence.items.TestItems.Instance;
import laustrup.bandwichpersistence.items.TestItems.Instances;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

class ModelServiceTests extends BandwichTester {

  @ParameterizedTest
  @EnumSource(EqualScenario.class)
  void canEqual(EqualScenario scenario) {
    test(() -> {
      Instance instance = Instance.initialise();
      Instances instances = arrange(new Instances(instance.Set_active(true), instance.Set_active(false)));
      Model<Identity<Signature.UUID>, Signature.UUID>
          first = define(instances.expected(), scenario, 0),
          second = define(instances.actual(), scenario, 1);
      boolean expected = scenario.is_expectedTrue();

      boolean actual = act(() -> ModelService.equals(first, second));

      asserting(expected)
          .is(actual);
    });
  }

  @Test
  void canToStringify() {
    Instance.Id uuid = Instance.Id.randomize();
    String title = "testTitle";
    boolean isActive = true;
    int amount = 9;

    test(() -> {
      Instance instance = arrange(new Instance(uuid, title, isActive, amount));
      String expected = String.format("\n" + """
          Instance{
              ids(
                  _id: %s
              ), elements(
                  _title: %s,
                  _active: %s,
                  _amount: %s
              )
          }""",
          uuid,
          title,
          isActive,
          amount
      );

      String actual = act(ModelService.toStringify(instance))
          .replace("\t", "    ");

      asserting(expected)
          .is(actual);
    });
  }

  private Model<Identity<Signature.UUID>, Signature.UUID> define(Instance instance, EqualScenario scenario, int index) {
    return switch (scenario) {
      case EQUALS -> Instance.toModel(instance);
      case NOT_EQUALS -> Instance.toModel(new Instance("Not an Instance"));
      case FIRST_NULL -> index == 0 ? null : Instance.toModel(instance);
      case SECOND_NULL -> index == 1 ? null : Instance.toModel(instance);
      case BOTH_NULL -> null;
    };
  }

  @AllArgsConstructor
  @Getter
  private enum EqualScenario {
    EQUALS(true),
    NOT_EQUALS(false),
    FIRST_NULL(false),
    SECOND_NULL(false),
    BOTH_NULL(true);

    private final boolean _expectedTrue;
  }
}