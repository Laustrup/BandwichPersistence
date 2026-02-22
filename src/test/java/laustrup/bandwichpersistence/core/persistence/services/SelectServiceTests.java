package laustrup.bandwichpersistence.core.persistence.services;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Properties;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;
import laustrup.bandwichpersistence.core.utilities.collections.Liszt;
import laustrup.bandwichpersistence.items.TestItems.Instance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.left;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.complying;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.selecting;
import static laustrup.bandwichpersistence.core.persistence.worm.services.NamingService.toColumnTitle;
import static laustrup.bandwichpersistence.core.services.ClassFieldService.getField;
import static laustrup.bandwichpersistence.core.services.StringService.firstCharacterAsUppercase;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

class SelectServiceTests extends BandwichTester {

  private final String
      _table = "test_instances",
      _alias = "testInstances";

  @Test
  void canSelectAll() {
    test(() -> {
      String expected = /*language=MySQL*/ String.format(
          "\nselect * from %s %s\n",
          _table,
          _alias
      );

      String actual = act(selecting(_table).select());

      asserting(expected)
          .is(actual);
    });
  }

  @Test
  void canSelectAllDatabaseFields() {
    test(() -> {
      String selections = Arrays.stream(Instance.class.getDeclaredFields())
          .map(field -> String.format("testInstances.%s TestInstance.%s",
              toColumnTitle(field),
              field.getName()
          )).collect(Collectors.joining(",\n\t")),
          expected = /*language=MySQL*/ String.format(
              "\nselect\n\t%s\nfrom %s %s\n",
              selections,
              _table,
              _alias
          );

      String actual = act(selecting(new Properties(Instance.class))
          .select()
      );

      asserting(expected)
          .is(actual);
    });
  }

  @Test
  void canSelectAllVariablesOfFields() {
    test(() -> {
      BiFunction<String, String, String> columnField = (clazz, column) -> String.format("%s.%s",
          clazz,
          column
      );
      String selections = "\ttestInstanceOwners.id TestInstanceOwner._id,\n" +
          Arrays.stream(Instance.Owner.class.getDeclaredFields())
              .flatMap(field -> Arrays.stream(field.getType().getDeclaredFields())
                  .map(fieldOfType -> String.format("\t%s Test%s",
                      columnField.apply(
                          "test" + firstCharacterAsUppercase(fieldOfType.getDeclaringClass().getSimpleName().toLowerCase()) + "s",
                          toColumnTitle(fieldOfType)
                      ),
                      columnField.apply(fieldOfType.getDeclaringClass().getSimpleName(), fieldOfType.getName())
                  ))).collect(Collectors.joining(",\n")) + "\n",
          fromTable = /*language=MySQL*/ "from test_instance_owners testInstanceOwners",
          innerJoin = /*language=MySQL*/ "left join test_instances testInstances on testInstanceOwners.id = testInstances.owner_id",
          expected = String.format(
              "\nselect\n%s%s\n%s\n",
              selections,
              fromTable,
              innerJoin
          );

      String actual = act(selecting(new Properties(Instance.Owner.class))
          .addJoin(left("test_instances",
              Condition.equals(
                  DatabaseField.of(getField(Instance.Owner.class, Instance.Owner.Fields._id.name())),
                  DatabaseField.of(getField(Instance.class, Instance.Fields._ownerId.name()))
              )
          ))
          .select()
      );

      asserting(expected)
          .is(actual);
    });
  }

  @Test
  void canSelectAllWhereCondition() {
    test(() -> {
      String expected = /*language=MySQL*/ String.format(
          "\nselect * from %s %s\nwhere %s.amount = %s.title\n",
          _table,
          _alias,
          _alias,
          _alias
      );
      Properties properties = arrange(new Properties(
          Instance.class,
          complying()
              .which(Condition.equals(
                  DatabaseField.of(
                      Instance.class,
                      getField(Instance.class, Instance.Fields._amount.name())
                  ), DatabaseField.of(
                      Instance.class,
                      getField(Instance.class, Instance.Fields._title.name())
                  )
              ))
      ));

      String actual = act(selecting(properties).select());

      asserting(expected)
          .is(actual);
    });
  }

  @Test
  void canSelectAllWhereConditionAndCondition() {
    test(() -> {
      String expected = /*language=MySQL*/ String.format(
          "\nselect * from %s %s\nwhere %s.amount = %s.title and %s.title = %s.amount\n",
          _table,
          _alias,
          _alias,
          _alias,
          _alias,
          _alias
      );
      Properties properties = arrange(new Properties(
          Instance.class,
          complying()
              .which(Condition.equals(
                  DatabaseField.of(
                      Instance.class,
                      getField(Instance.class, Instance.Fields._amount.name())
                  ), DatabaseField.of(
                      Instance.class,
                      getField(Instance.class, Instance.Fields._title.name())
                  )
              ))
              .and(Condition.equals(
                  DatabaseField.of(
                      Instance.class,
                      getField(Instance.class, Instance.Fields._title.name())
                  ), DatabaseField.of(
                      Instance.class,
                      getField(Instance.class, Instance.Fields._amount.name())
                  )
              ))
      ));

      String actual = act(selecting(properties).select());

      asserting(expected)
          .is(actual);
    });
  }

  @Test
  void canSelectAllInnerJoin() {
    test(() -> {
      String expected = /*language=MySQL*/ arrange(String.format(
          "\nselect * from %s %s\ninner join %s %s on %s.title = %s.amount\n",
          _table,
          _alias,
          _table,
          _alias,
          _alias,
          _alias
      ));

      String actual = act(selecting(_table)
          .addJoin(Join.inner(
              Instance.class,
              DatabaseField.of(
                  Instance.class,
                  getField(Instance.class, Instance.Fields._title.name())
              ), DatabaseField.of(
                  Instance.class,
                  getField(Instance.class, Instance.Fields._amount.name())
              )
          ))
          .select()
      );

      asserting(expected)
          .is(actual);
    });
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void canOrderGroupings(int expected) {
    test(() -> {
      Function<String[], DatabaseField[]> getDatabaseFields = fieldNames -> Arrays.stream(fieldNames)
          .map(fieldName -> DatabaseField.of(getField(Instance.class, fieldName)))
          .toArray(DatabaseField[]::new);
      Liszt<DatabaseField> arrangement = arrange(Liszt.of(getDatabaseFields.apply(switch (expected) {
        case -1 -> new String[]{Instance.Fields._id.name(), Instance.Fields._ownerId.name()};
        case 0 ->  new String[]{Instance.Fields._id.name(), Instance.Fields._id.name()};
        case 1 ->  new String[]{Instance.Fields._ownerId.name(), Instance.Fields._id.name()};
        default -> throw new IllegalStateException("Unknown order grouping expected: " + expected);
      })));

      int actual = act(Properties.Selections.orderGroupings(arrangement.getFirst(), arrangement.getLast()));

      asserting(expected)
          .is(actual);
    });
  }
}