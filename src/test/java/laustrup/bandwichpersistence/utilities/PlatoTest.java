package laustrup.bandwichpersistence.utilities;

import laustrup.bandwichpersistence.JTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PlatoTest extends JTest {

    private Plato _plato;

    @ParameterizedTest
    @CsvSource(value = {
            "Argument.TRUE",
            "Argument.FALSE",
            "Argument.UNDEFINED",
            "true", "false"
            })
    public void declaringArgumentsTest(String declaration) {
        //ARRANGE
        boolean bool = false;
        boolean valueIsAnArgument = true;
        Plato.Argument argument = null;

        switch (declaration) {
            case "Argument.TRUE":
                argument = Plato.Argument.TRUE;
                break;
            case "Argument.FALSE":
                argument = Plato.Argument.FALSE;
                break;
            case "Argument.UNDEFINED":
                argument = Plato.Argument.UNDEFINED;
                break;
            case "true":
                bool = true;
                valueIsAnArgument = false;
                break;
            case "false":
                valueIsAnArgument = false;
                break;
            default:
        }

        if (valueIsAnArgument) assertAndActArgument(argument);
        else assertAndActBool(bool);
    }

    private void assertAndActArgument(Plato.Argument argument) {
        // ACT
        begin();
        _plato = new Plato(argument);
        calculatePerformance();

        // ASSERT
        assertEquals(argument, _plato.get_argument());
        assertEquals(argument == Plato.Argument.TRUE, _plato.get_truth());
    }

    private void assertAndActBool(boolean bool) {
        // ACT
        begin();
        _plato = new Plato(bool);
        calculatePerformance();

        // ASSERT
        assertEquals(bool, _plato.get_argument() == Plato.Argument.TRUE);
        assertEquals(bool, _plato.get_truth());
    }
}