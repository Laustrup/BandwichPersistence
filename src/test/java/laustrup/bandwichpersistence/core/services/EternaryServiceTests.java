package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.services.EternaryService.Operator.Property;
import lombok.Getter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

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
        test(() -> delegate(ifNotNull(arrange(null)), scenario, notExpectations(scenario)));
        test(() -> delegate(ifNotNull(arrange("Not null")), scenario, _then));
    }

    @ParameterizedTest
    @EnumSource(Scenario.class)
    void canCheckIfIsSame(Scenario scenario) {
        String string = "string";
        String notExpectations = notExpectations(scenario);

        test(() -> delegate(arrange(isSame(string, string)), scenario, _then));
        test(() -> delegate(arrange(isSame(string, string, string)), scenario, _then));
        test(() -> delegate(arrange(isSame(string, "Not String")), scenario, notExpectations));
        test(() -> delegate(arrange(isSame(string, string, "Not String")), scenario, notExpectations));
    }

    @ParameterizedTest
    @EnumSource(EmptinessScenario.class)
    void canCheckIfIsEmpty(EmptinessScenario scenario) {
        test(() -> {
            String
                    then = "then",
                    orElse = "orElse",
                    arrangement = arrange(scenario.get_arrangement());

            String expected;

            if (arrangement == null)
                expected = then;
            else
                expected = arrangement.equals(EmptinessScenario.EMPTY.get_arrangement())
                        ? then
                        : orElse;

            String actual = act(EternaryService.ifEmpty(arrangement).then(then).orElse(orElse));

            asserting(actual)
                    .is(expected);
        });
    }

    @ParameterizedTest
    @EnumSource(EmptinessScenario.class)
    void canCheckIfIsNotEmpty(EmptinessScenario scenario) {
        test(() -> {
            String
                    otherwise = "otherwise",
                    arrangement = arrange(scenario.get_arrangement());

            String expected;

            if (arrangement == null)
                expected = otherwise;
            else
                expected = arrangement.equals(EmptinessScenario.NOT_EMPTY.get_arrangement())
                        ? arrangement
                        : otherwise;

            String actual = act(EternaryService.ifNotEmpty(arrangement).otherwise(otherwise));

            asserting(actual)
                    .is(expected);
        });
    }

    private void delegate(Eternary eternary, Scenario scenario, String expected) {
        switch (scenario) {
            case THEN -> then(eternary, expected);
            case OR -> or(eternary, expected);
            case OR_ELSE -> orElse(eternary, expected);
        }
    }

    private void then(Eternary eternary, String expected) {
        Binder<String> binder = act(eternary.then(_then));
        String actual = binder.orElse(_orElse);

        asserting(new Binder<>(new Property<>(_then, _then.equals(expected)))).compare(binder);
        asserting(expected).is(actual);
    }

    private void or(Eternary eternary, String expected) {
        Binder<String> binder = act(eternary
                .then(_then)
                .or(_or, expected::equals)
        );
        String actual = binder.orElse(_orElse);

        asserting((getPropertyFromOption(binder, _or).is_success() == expected.equals(_or))).isTrue();
        asserting(expected).is(actual);
    }

    private void orElse(Eternary eternary, String expected) {
        String actual = act(eternary.then(_then)
                .or(_or, String::isEmpty)
                .orElse(_orElse)
        );

        asserting(expected).is(actual::equals);
    }

    private Property<String> getPropertyFromOption(Binder<String> actual, String option) {
        return actual.get_properties().stream()
                .filter(property -> property.get_option().equals(option))
                .findFirst()
                .orElseThrow();
    }

    private String notExpectations(Scenario scenario) {
        return switch (scenario) {
            case THEN, OR_ELSE -> _orElse;
            case OR -> _or;
        };
    }

    private enum Scenario {
        THEN,
        OR,
        OR_ELSE
    }

    private enum EmptinessScenario {
        EMPTY(""),
        NOT_EMPTY("not empty"),
        NULL(null);

        @Getter
        private final String _arrangement;

        EmptinessScenario(String arrangement) {
            _arrangement = arrangement;
        }
    }
}