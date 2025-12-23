package laustrup.bandwichpersistence.core.persistence.models;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.persistence.worm.models.DatabaseDefinition;
import laustrup.bandwichpersistence.items.TestItems;
import org.junit.jupiter.api.Test;

import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

class EntityDataCollectionTests extends BandwichTester {

  private EntityDataCollection dataCollection = TestItems.EntityDataCollection.get_instance();

  @Test
  void canGetEntity() {
    test(() -> {
      Class<TestItems.Instance> arrangement = arrange(TestItems.Instance.class);
      DatabaseDefinition expected = new DatabaseDefinition.Entity(arrangement);

      DatabaseDefinition actual = act(dataCollection.entityOf(arrangement));

      asserting(expected)
          .isNotNull()
          .isIdenticalTo(actual);
    });
  }
}