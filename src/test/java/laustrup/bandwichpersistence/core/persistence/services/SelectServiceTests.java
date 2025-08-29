package laustrup.bandwichpersistence.core.persistence.services;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Properties;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;
import org.junit.jupiter.api.Test;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition.Equation.EQUALS;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.complying;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.selecting;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

class SelectServiceTests extends BandwichTester {

    private final String _table = "table";

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
            String expected = /*language=MySQL*/ String.format("\nselect * from %s\nwhere this.thing = that.thing\n", _table);
            Properties properties = arrange(new Properties(
                    _table,
                    complying()
                            .which(new Condition(DatabaseField.of("this", "thing"), EQUALS, DatabaseField.of("that", "thing")))
            ));

            String actual = act(selecting(properties).select());

            asserting(expected)
                    .is(actual);
        });
    }

    @Test
    void canSelectAllWhereConditionAndCondition() {
        test(() -> {
            String expected = /*language=MySQL*/ String.format("\nselect * from %s\nwhere this.thing = that.thing and this.other = that.other\n", _table);
            Properties properties = arrange(new Properties(
                    _table,
                    complying()
                            .which(new Condition(DatabaseField.of("this", "thing"), EQUALS, DatabaseField.of("that", "thing")))
                            .and(new Condition(DatabaseField.of("this", "other"), EQUALS, DatabaseField.of("that", "other")))
            ));

            String actual = act(
                    selecting(properties)
                            .select()
            );

            asserting(expected)
                    .is(actual);
        });
    }

    @Test
    void canSelectAllInnerJoin() {
        test(() -> {
            String joinTable = "join_table";
            String alias = "joinTable";
            String row = "id";
            String expected = /*language=MySQL*/ arrange(String.format(
                    "\nselect * from %s\ninner join join_table joinTable on joinTable.id = %s.id\n", _table, _table)
            );

            String actual = act(selecting(_table)
                    .addJoin(Join.inner(joinTable, new DatabaseField(alias, row), new DatabaseField(_table, row)))
                    .select()
            );

            asserting(expected)
                    .is(actual);
        });
    }
}