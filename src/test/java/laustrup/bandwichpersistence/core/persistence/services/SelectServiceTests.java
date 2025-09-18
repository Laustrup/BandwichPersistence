package laustrup.bandwichpersistence.core.persistence.services;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Properties;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;
import laustrup.bandwichpersistence.items.TestItems;
import org.junit.jupiter.api.Test;

import static laustrup.bandwichpersistence.core.persistence.services.DatabaseTableService.defineTitle;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.complying;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.selecting;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

class SelectServiceTests extends BandwichTester {

    private final String _table = defineTitle(DatabaseField.Table.class.getSimpleName());

    @Test
    void canSelectAll() {
        test(() -> {
            String expected = arrange(/*language=MySQL*/ "\nselect * from " + _table + "\n");

            String actual = act(selecting(_table).select());

            asserting(expected)
                    .is(actual);
        });
    }

    @Test
    void canSelectAllWhereCondition() {
        test(() -> {
            String expected = /*language=MySQL*/ String.format("\nselect * from %s\nwhere test_instances.amount = test_instances.title\n", _table);
            Properties properties = arrange(new Properties(
                    _table,
                    complying()
                            .which(Condition.equals(
                                    DatabaseField.of(new DatabaseField.Configuration(
                                            TestItems.Instance.class,
                                            TestItems.Instance.Fields._amount
                                    )), DatabaseField.of(new DatabaseField.Configuration(
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
                    "\nselect * from %s\nwhere test_instances.amount = test_instances.email and test_instances.email = that.amount\n",
                    _table
            );
            Properties properties = arrange(new Properties(
                    _table,
                    complying()
                            .which(Condition.equals(
                                    DatabaseField.of(new DatabaseField.Configuration(
                                            TestItems.Instance.class,
                                            TestItems.Instance.Fields._amount
                                    )), DatabaseField.of(new DatabaseField.Configuration(
                                            TestItems.Instance.class,
                                            TestItems.Instance.Fields._title
                                    ))
                            ))
                            .and(Condition.equals(
                                    DatabaseField.of(new DatabaseField.Configuration(
                                            TestItems.Instance.class,
                                            TestItems.Instance.Fields._title
                                    )), DatabaseField.of(new DatabaseField.Configuration(
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
                    "\nselect * from %s\ninner join %s joinTable on joinTable.id = %s.id\n", _table, _table, _table)
            );

            String actual = act(selecting(_table)
                    .addJoin(Join.inner(
                            TestItems.Instance.class.getSimpleName(),
                            DatabaseField.of(new DatabaseField.Configuration(
                                    TestItems.Instance.class,
                                    TestItems.Instance.Fields._title
                            )), DatabaseField.of(new DatabaseField.Configuration(
                                    TestItems.Instance.class,
                                    TestItems.Instance.Fields._amount
                            ))))
                    .select()
            );

            asserting(expected)
                    .is(actual);
        });
    }
}