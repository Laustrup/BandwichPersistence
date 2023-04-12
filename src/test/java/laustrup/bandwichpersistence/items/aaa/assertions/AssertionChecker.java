package laustrup.bandwichpersistence.items.aaa.assertions;

import laustrup.bandwichpersistence.utilities.collections.lists.Liszt;

/**
 * Will check for assertion scenarios such as is null or different length of inputs.
 */
public class AssertionChecker {

    /**
     * Checks if either of the inputs are either null or have different length.
     * @param expectations The collection of expectations generated.
     * @param actuals The collection of results from acting methods generated.
     * @return The AssertionMessage depending on the scenario, ERROR if it couldn't find any solution.
     */
    static String collectionMessage(Object[] expectations, Object[] actuals) {
        if (expectations == null || actuals == null) return AssertionMessage.IS_NULL.get_content();
        if (expectations.length != actuals.length) return AssertionMessage.LENGTH_IS_DIFFERENT.get_content();

        return AssertionMessage.ASSERTION_ERROR.get_content();
    }

    /**
     * Checks if either of the inputs are null.
     * @param expected The Object that is generated to be expected.
     * @param actual The Object of the result from the acting method.
     * @return The AssertionMessage depending on the scenario, ERROR if it couldn't find any solution.
     */
    static String objectMessage(Object expected, Object actual) {
        return expected == null || actual == null
            ? AssertionMessage.IS_NULL.get_content()
            : AssertionMessage.ASSERTION_ERROR.get_content();
    }

    @SuppressWarnings("unchecked")
    static String lisztMessage(Object expectations, Object actuals) {
        return expectations == null || actuals == null
            ? AssertionMessage.IS_NULL.get_content()
            : ((Liszt<Object>) expectations).size() != ((Liszt<Object>) actuals).size()
                ? AssertionMessage.LENGTH_IS_DIFFERENT.get_content()
                : AssertionMessage.ASSERTION_ERROR.get_content();
    }

    /**
     * Checks if the ids should be allowed to be asserted.
     * @param expected The id that is generated to expectation.
     * @param actual The id of results from action methods generated.
     * @return True if they should be allowed to be asserted.
     */
    static boolean allowId(long expected, long actual) {
        return expected > 0 && actual > 0;
    }

    /**
     * Checks if the inputs should be allowed to be asserted.
     * @param expectations The collection of expectations generated.
     * @param actuals The collection of results from acting methods generated.
     * @return True if they should be allowed to be asserted.
     */
    static boolean allowCollection(Object[] expectations, Object[] actuals) {
        return (expectations == null || actuals == null) || expectations.length != actuals.length;
    }
    /**
     * Checks if the inputs should be allowed to be asserted.
     * @param expectations The collection of expectations generated.
     * @param actuals The collection of results from acting methods generated.
     * @return True if they should be allowed to be asserted.
     */
    @SuppressWarnings("unchecked")
    static boolean allowLiszt(Object expectations, Object actuals) {
        return allowObjects(expectations,actuals) && ((Liszt<Object>) expectations).size() == ((Liszt<Object>) actuals).size();
    }

    /**
     * Checks if the inputs should be allowed to be asserted.
     * @param expected The Object that is generated to be expected.
     * @param actual The Object of the result from acting method.
     * @return True if they should be allowed to be asserted.
     */
    static boolean allowObjects(Object expected, Object actual) {
        return (expected == null || actual == null);
    }
}
