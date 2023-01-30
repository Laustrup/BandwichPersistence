package laustrup.bandwichpersistence.services;

import java.util.Random;

public class RandomCreatorService {

    /**
     * Random utility for making random elements.
     */
    private Random _random = new Random();

    /**
     * Singleton instance of the Service.
     */
    private static RandomCreatorService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static RandomCreatorService get_instance() {
        if (_instance == null) _instance = new RandomCreatorService();
        return _instance;
    }

    private RandomCreatorService() {}

    /**
     * Generates a random.
     * @return The generated string.
     */
    public String generateString(boolean uniqueCharacter, int length) {
        int min = !uniqueCharacter ? 97 : 123, // letter a
            max = !uniqueCharacter ? 122 : 122 * 2; // letter z
        StringBuilder buffer = new StringBuilder(length);

        for (int i = 0; i < length; i++)
            buffer.append((char) (min + (int) (_random.nextFloat() * (max - min + 1))));

        return buffer.toString();
    }

    /**
     * Will generate a substring from another String.
     * @param string The String that the substring will be generated from.
     * @return The generated substring.
     */
    public String generateSubString(String string) {
        if (string.length() > 1) {
            int start = _random.nextInt(string.length())+1;
            int end = _random.nextInt(start)+1;

            while (start>end) {
                start = _random.nextInt(string.length())+1;
                end = _random.nextInt(start)+1;
            }

            return string.substring(start,end);
        }
        else
            return string;
    }

    public String generatePassword() {
        String password = new String();

        password += generateString(false,5);
        password += generateString(true, 2);
        password += generateString(false,3);
        password += generateString(true,2);
        password += 1;

        return password;
    }

    /**
     * Generates an integer into a new integer, that isn't with the same value.
     * @param integer The integer current value.
     * @param bound The limit of the highest possible value.
     * @return The generated integer. If bound isn't larger than 1, it will return the same integer value.
     */
    public int generateDifferent(int integer, int bound) {
        int generated = _random.nextInt(bound);

        if (bound > 1)
            while (generated == integer)
                generated = _random.nextInt(bound);

        return generated;
    }
}
