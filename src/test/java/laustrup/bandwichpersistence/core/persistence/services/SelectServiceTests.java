package laustrup.bandwichpersistence.core.persistence.services;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Properties;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Properties.Selections;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;
import laustrup.bandwichpersistence.items.TestItems;
import org.junit.jupiter.api.Test;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.stream.Collectors;

import static laustrup.bandwichpersistence.core.persistence.DatabaseField.Configuration.databaseFieldConfiguration;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.complying;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.selecting;
import static laustrup.bandwichpersistence.core.services.EternaryService.ifNotNull;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

class SelectServiceTests extends BandwichTester {

  private final String
      _table = "test_instances",
      _alias = "testInstances";

  @Test
  void canSelectAll() {
    canSelect(
        /*language=MySQL*/ String.format(
            "\nselect * from %s %s\n",
            _table,
            _alias
        ),
        _table,
        null
    );
  }

  @Test
  void canSelectAllDatabaseFields() {
    String selections = Arrays.stream(TestItems.Instance.class.getDeclaredFields())
        .map(field -> String.format("testInstances.%s", field.getName().replace("_", "")))
        .collect(Collectors.joining(",\n\t"));

    canSelect(
        /*language=MySQL*/ String.format(
            "\nselect\n\t%s\nfrom %s %s\n",
            selections,
            _table,
            _alias
        ),
        null,
        new Properties(
            Selections.of(TestItems.Instance.class),
            TestItems.Instance.class
        )
    );
  }

  private void canSelect(
      String arrangement,
      @Nullable String table,
      @Nullable Properties properties
  ) {
    test(() -> {
      String expected = arrange(arrangement);

      String actual = act(ifNotNull(table)
          .then(() -> selecting(table))
          .orElse(() -> selecting(properties))
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
          TestItems.Instance.class,
          complying()
              .which(Condition.equals(
                  DatabaseField.of(databaseFieldConfiguration(
                      TestItems.Instance.class,
                      TestItems.Instance.Fields._amount
                  )), DatabaseField.of(databaseFieldConfiguration(
                      TestItems.Instance.class,
                      TestItems.Instance.Fields._title
                  ))
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
          TestItems.Instance.class,
          complying()
              .which(Condition.equals(
                  DatabaseField.of(databaseFieldConfiguration(
                      TestItems.Instance.class,
                      TestItems.Instance.Fields._amount
                  )), DatabaseField.of(databaseFieldConfiguration(
                      TestItems.Instance.class,
                      TestItems.Instance.Fields._title
                  ))
              ))
              .and(Condition.equals(
                  DatabaseField.of(databaseFieldConfiguration(
                      TestItems.Instance.class,
                      TestItems.Instance.Fields._title
                  )), DatabaseField.of(databaseFieldConfiguration(
                      TestItems.Instance.class,
                      TestItems.Instance.Fields._amount
                  ))
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
              TestItems.Instance.class,
              DatabaseField.of(databaseFieldConfiguration(
                  TestItems.Instance.class,
                  TestItems.Instance.Fields._title
              )), DatabaseField.of(databaseFieldConfiguration(
                  TestItems.Instance.class,
                  TestItems.Instance.Fields._amount
              ))
          ))
          .select()
      );

      asserting(expected)
          .is(actual);
    });
  }
}