package laustrup.bandwichpersistence.core.services.persistence.queries;

import laustrup.bandwichpersistence.core.services.StringService;

public class FormatQueryService {

    public static final String formatReplacementCharacter = "%5";

    public static String formatQuery(String query, String... inputs) {
        int inputIndex = 0;

        for (int i = 0; i < query.length(); i++) {
            if (replacementCharacterFound(query, i)) {
                query = StringService.replace(i - 1, i + 1, query, inputs[inputIndex]);
                inputIndex++;
            }
        }

        if (inputs.length != inputIndex)
            throw new RuntimeException(String.format("""
                    When formatting query %n%n%s the amount of inputs "%s" doesn't match "%s" amounts
                    """,
                    query,
                    inputs.length,
                    inputIndex
            ));

        return query;
    }

    private static boolean replacementCharacterFound(String query, int index) {
        return index > 0 &&
                query.charAt(index - 1) == formatReplacementCharacter.charAt(0) &&
                query.charAt(index) == formatReplacementCharacter.charAt(1);
    }
}
