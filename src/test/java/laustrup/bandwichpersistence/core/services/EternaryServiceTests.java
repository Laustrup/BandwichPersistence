package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.services.EternaryService.Operator.Property;
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

    private void delegate(Eternary eternary, Scenario scenario, boolean expectedCondition) {
        switch (scenario) {
            case THEN -> then(eternary, expectedCondition);
            case OR -> or(eternary, expectedCondition);
            case OR_ELSE -> orElse(eternary, expectedCondition);
        }
    }

    private void then(Eternary eternary, boolean expectedCondition) {
        Binder<String> binder = act(eternary.then(_then));
        String actual = binder.orElse(_orElse);

        asserting(new Binder<>(new Property<>(_then, expectedCondition)))
                .isEqualTo(binder);
        asserting(actual).isEqualTo(_then);
    }

    private void or(Eternary eternary, boolean expectedCondition) {
        Binder<String> binder = act(eternary.then(_then)
                .or(_or, or -> or.equals(_or))
        );
        String actual = binder.orElse(_orElse);

        asserting((getPropertyFromOption(binder, _or).is_success() == expectedCondition))
                .isTrue();
        asserting(actual).isEqualTo(_or);
    }

    private void orElse(Eternary eternary, boolean expectedCondition) {
        String actual = act(eternary.then(_then)
                .or(_or, String::isEmpty)
                .orElse(_orElse)
        );

        asserting(_orElse)
                .is(expected -> actual.equals(expected) == expectedCondition);
    }

    @SuppressWarnings("unchecked")
    private Property<String> getPropertyFromOption(Binder<String> actual, String option) {
        return ((Liszt<Property<String>>) getFieldValue(actual, Operator.Fields._properties)).stream()
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