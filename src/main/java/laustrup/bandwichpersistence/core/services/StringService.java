package laustrup.bandwichpersistence.core.services;

import lombok.Getter;

import java.util.Collection;
import java.util.Random;

public class StringService {

    private static final String _numerals = "0123456789";

    private static final String _letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final Random _random = new Random();

    public static String replace(int start, int end, String string, String replacement) {
        return string.substring(0, start) + replacement + string.substring(end);
    }

    public static String randomString() {
        return generateRandom(_random.nextInt(_numerals.length() + _letters.length()) + 1);
    }

    public static String generateRandom(int length) {
        return generateRandom(length, null);
    }

    public static String generateRandom(int length, Configuration configuration) {
        String options = generateOptions(configuration);
        StringBuilder string = new StringBuilder();

        for (int i = 0; i < length; i++)
            string.append(options.charAt(_random.nextInt(options.length())));

        return string.toString();
    }

    private static String generateOptions(Configuration configuration) {
        if (configuration == null)
            return _numerals + _letters;

        StringBuilder options = new StringBuilder();

        if (configuration.is_letters()) {
            options.append(_letters);

            if (configuration.is_uppercase() != configuration.is_lowercase()) {
                char[] chars = options.toString().toCharArray();

                for (int i = 0; i < chars.length; i++)
                    chars[i] = configuration.is_uppercase()
                            ? Character.toUpperCase(chars[i])
                            : Character.toLowerCase(chars[i]);

                options = new StringBuilder(String.valueOf(chars));
            }
        }

        if (configuration.is_numerals())
            options.append(_numerals);


        return options.toString();
    }

    public static boolean containsAny(String string, Collection<String> collection) {
        for (String item : collection)
            if (string.contains(item))
                return true;
        return false;
    }

    public static String fieldToColumnName(String field) {
        if (field == null)
            return null;

        if (field.charAt(0) == '_')
            field = field.substring(1);

        for (int i = 0; i < field.length(); i++) {
            char character = field.charAt(i);

            if (Character.isUpperCase(character)) {
                field = field.replace(String.valueOf(character), (i != 0 ? "_" : "") + Character.toLowerCase(character));
                if (i != 0)
                    i++;
            }
        }

        return field;
    }

    @Getter
    public static class Configuration {

        private final boolean _numerals;

        public boolean _letters;

        public boolean _uppercase = true;

        public boolean _lowercase = true;

        public Configuration(boolean numerals, boolean letters) {
            _numerals = numerals;
            _letters = letters;
        }

        public Configuration(boolean numerals, boolean letters, boolean uppercase, boolean lowercase) {
            _numerals = numerals;
            _letters = letters;
            _uppercase = uppercase;
            _lowercase = lowercase;
        }
    }
}
