package laustrup.bandwichpersistence.items.aaa.assertions;

import java.util.function.Function;

import static laustrup.bandwichpersistence.items.aaa.assertions.AssertionFailer.failing;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Is used to perform assertions for the asserter.
 */
public class AssertionActor {

    /**
     * Will use the function to assert items.
     * If the return of the function is not SUCCESS.get_content(),
     * it will fail with the return as a message.
     * @param function The function that will assert.
     */
    static void doAssert(Function<Object,String> function) { doAssert(null, function); }

    /**
     * Will use the function to assert items.
     * If the return of the function is not SUCCESS.get_content(),
     * it will fail with the return as a message.
     * @param input The input for the function
     * @param function The function that will assert.
     */
    static void doAssert(Object input, Function<Object,String> function) {
        String message = function.apply(input);
        if (!message.equals(AssertionMessage.SUCCESS.get_content()))
            failing(message);
    }

    /**
     * Will only assert expected and actual if statement is true.
     * @param statement The statement that decides if the assertion should occur.
     * @param expected The object that is arranged to be expected the same as actual.
     * @param actual The Object that is the result of an acted method.
     */
    static void assertIf(boolean statement, Object expected, Object actual) {
        if (statement)
            assertEquals(expected, actual);
    }

    /**
     * Will assert in a for loop.
     * @param index Is an initialized integer, that will be incremented for each iteration.
     * @param amount The amount the index is allowed to be less or equal to.
     * @param assertion The function that will assert, the input is the index.
     */
    static void assertFor(int index, int amount, Function<Integer,Object> assertion) {
        for (; index <= amount; index++)
            assertion.apply(index);
    }

    /**
     * Will assert in a for loop.
     * @param indexes The inputs for the for loop with indexes, the first should be reserved for the iteration index.
     * @param amount The amount the index is allowed to be less or equal to.
     * @param assertion The function that will assert, the input is the index.
     */
    static void assertFor(int[] indexes, int amount, Function<Integer[],Object> assertion) {
        int index = indexes[0];
        for (; index <= amount; index++) {
            Integer[] input = new Integer[indexes.length];
            for (int j = 0; j < input.length; j++)
                input[j] = j > 0 ? indexes[j] : index;

            assertion.apply(input);
        }
    }
}
