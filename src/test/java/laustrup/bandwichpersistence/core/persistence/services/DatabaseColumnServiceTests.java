package laustrup.bandwichpersistence.core.persistence.services;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import laustrup.bandwichpersistence.core.utilities.collections.Liszt;
import laustrup.bandwichpersistence.items.TestItems;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static laustrup.bandwichpersistence.core.persistence.services.DatabaseColumnService.getIdColumnsOf;
import static laustrup.bandwichpersistence.core.persistence.services.DatabaseColumnService.get_columns;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

class DatabaseColumnServiceTests extends BandwichTester {

  @Test
  void canGetColumns() {
    test(() -> {
      Class<TestItems.Instance.Child> clazz = TestItems.Instance.Child.class;
      Stream<? extends Member> members = Stream.concat(
          Arrays.stream(clazz.getDeclaredFields()),
          Arrays.stream(TestItems.Instance.class.getDeclaredFields())
      );
      Map<? extends Member, DatabaseField> expected = arrange(members.collect(Collectors.toMap(
          Function.identity(),
          member -> DatabaseField.of(clazz, member)
      )));

      Map<? extends Member, DatabaseField> actual = act(get_columns(clazz));

      expected.forEach((key, value) ->
          asserting(value.equals(actual.get(key)))
            .isTrue()
      );
    });
  }

  @Test
  void canGetIdColumns() {
    test(() -> {
      Liszt<Table.Column> actuals = act(Liszt.of(getIdColumnsOf(TestItems.Instance.class)));

      asserting(actuals)
          .isNotNull()
          .isNotEmpty()
          .allMatches(Table.Column::isPrimary);
    });
  }
}