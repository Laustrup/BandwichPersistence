package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.utilities.collections.Liszt;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static laustrup.bandwichpersistence.core.services.ObjectService.getFieldValue;
import static laustrup.bandwichpersistence.core.services.EternaryService.*;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

class EternaryServiceTests extends BandwichTester {

    private final String
            _then = "then",
            _or = "or",
            _orElse = "or else";

    @ParameterizedTest
    @EnumSource(Scenario.class)
    void canCheckIfNotNull(Scenario scenario) {
        test(() -> delegate(ifNotNull(arrange(null)), scenario, false));
        test(() -> delegate(ifNotNull(arrange("Not null")), scenario, true));
    }

    @ParameterizedTest
    @EnumSource(Scenario.class)
    void canCheckIfIsSame(Scenario scenario) {
        String string = "string";
        test(() -> delegate(arrange(isSame(string, string)), scenario, true));
        test(() -> delegate(arrange(isSame(string, string, string)), scenario, true));
        test(() -> delegate(arrange(isSame(string, "Not String")), scenario, false));
        test(() -> delegate(arrange(isSame(string, string, "Not String")), scenario, false));
    }

    private void delegate(Eternary<String> ETernary, Scenario scenario, boolean expectedCondition) {
        switch (scenario) {
            case THEN -> then(ETernary, expectedCondition);
            case OR -> or(ETernary, expectedCondition);
            case OR_ELSE -> orElse(ETernary, expectedCondition);
        }
    }

    private void then(Eternary<String> ETernary, boolean expectedCondition) {
        Eternary.Binder<String> binder = act(ETernary.then(_then));
        String actual = binder.orElse(_orElse);

        asserting(new Eternary.Binder<>(new Eternary.Operator.Properties<>(_then, expectedCondition)))
                .isEqualTo(binder);
        asserting(actual).isEqualTo(_then);
    }

    private void or(Eternary<String> ETernary, boolean expectedCondition) {
        Eternary.Binder<String> binder = act(ETernary.then(_then).or(true, _or));
        String actual = binder.orElse(_orElse);

        asserting((getPropertyFromOption(binder, _or).is_condition() == expectedCondition))
                .isTrue();
        asserting(actual).isEqualTo(_or);
    }

    private void orElse(Eternary<String> ETernary, boolean expectedCondition) {
        String actual = act(ETernary.then(_then).or(false, _or).orElse(_orElse));

        asserting(_orElse)
                .is(expected -> actual.equals(expected) == expectedCondition);
    }

    @SuppressWarnings("unchecked")
    private Eternary.Operator.Properties<String> getPropertyFromOption(Eternary.Binder<String> actual, String option) {
        return ((Liszt<Eternary.Operator.Properties<String>>) getFieldValue(actual, Eternary.Operator.Fields._properties)).stream()
                .filter(property -> property.get_option().equals(option))
                .findFirst()
                .orElseThrow();
    }

    public enum Scenario {
        THEN,
        OR,
        OR_ELSE
    }
}