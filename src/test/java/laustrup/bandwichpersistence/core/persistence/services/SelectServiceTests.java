package laustrup.bandwichpersistence.core.persistence.services;

import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Properties;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;
import laustrup.bandwichpersistence.quality_assurance.Tester;
import org.junit.jupiter.api.Test;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.*;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.Area.INNER;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition.Equation.EQUALS;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.complying;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

class SelectServiceTests extends Tester {

    private final String _table = "table";

    @Test
    void canSelectAll() {
        test(() -> {
            String expected = arrange(/*language=MySQL*/ "select * from " + _table);

            String actual = act(
                    selecting(_table)
                            .select()
            );

            asserting(expected)
                    .isEqualTo(actual);
        });
    }

    @Test
    void canSelectAllWhereCondition() {
        test(() -> {
            String expected = /*language=MySQL*/ String.format("select * from %s where this.thing = that.thing", _table);
            Properties properties = arrange(new Properties(
                    _table,
                    complying()
                            .that(new Condition(Field.of("this", "thing"), EQUALS, Field.of("that", "thing")))
            ));

            String actual = act(
                    selecting(properties)
                            .select()
            );

            asserting(expected)
                    .isEqualTo(actual);
        });
    }

    @Test
    void canSelectAllWhereConditionAndCondition() {
        test(() -> {
            String expected = /*language=MySQL*/ String.format("select * from %s where this.thing = that.thing and this.other = that.other", _table);
            Properties properties = arrange(new Properties(
                    _table,
                    complying()
                            .that(new Condition(Field.of("this", "thing"), EQUALS, Field.of("that", "thing")))
                            .and(new Condition(Field.of("this", "other"), EQUALS, Field.of("that", "other")))
            ));

            String actual = act(
                    selecting(properties)
                            .select()
            );

            asserting(expected)
                    .isEqualTo(actual);
        });
    }

    @Test
    void canSelectAllInnerJoin() {
        test(() -> {
            String alias = "joinTable";
            String row = "id";
            String expected = /*language=MySQL*/ arrange(String.format("select * from %s inner join join_table joinTable on joinTable.id = %s.id", _table, _table));

            String actual = act(
                    selecting(_table)
                            .addJoin(new Join(
                                    INNER,
                                    alias,
                                    new Field("join_table", row),
                                    new Field(_table, row)
                            ))
                            .select()
            );

            asserting(expected)
                    .isEqualTo(actual);
        });
    }
}