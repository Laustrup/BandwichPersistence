package laustrup.bandwichpersistence.core.services;

import lombok.Getter;

import java.util.Collection;
import java.util.Random;

import static laustrup.bandwichpersistence.core.services.EternaryService.stating;

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

  public static String firstCharacterAsUppercase(String string) {
    return firstCharacterCaseHandle(string, true);
  }

  public static String firstCharacterAsLowercase(String string) {
    return firstCharacterCaseHandle(string, false);
  }

  private static String firstCharacterCaseHandle(String string, boolean asUppercase) {
    String firstCharacter = string.substring(0, 1);

    return stating(asUppercase)
        .then(firstCharacter::toUpperCase)
        .orElse(firstCharacter::toLowerCase) + string.substring(1);
  }

  public static String upperCasesToLowerCaseWithUnderscore(String string) {
    for (char character : string.toCharArray())
      if (Character.isUpperCase(character))
        string = string.replace(String.valueOf(character), "_" + Character.toLowerCase(character));

    return string;
  }

  public static String firstCharacterAsLowerCase(String string) {
    if (string == null || string.isEmpty())
      return "";

    return string.substring(0, 1).toLowerCase() + string.substring(1);
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
