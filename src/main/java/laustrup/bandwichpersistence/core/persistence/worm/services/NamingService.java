package laustrup.bandwichpersistence.core.persistence.worm.services;

import laustrup.bandwichpersistence.core.persistence.models.members.IdReferenceMember;
import laustrup.bandwichpersistence.core.services.EternaryService;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Member;

import static laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseDefinitionService.intendedNameOf;
import static laustrup.bandwichpersistence.core.services.EternaryService.stating;
import static laustrup.bandwichpersistence.core.services.StringService.firstCharacterAsLowercase;
import static laustrup.bandwichpersistence.core.services.StringService.upperCasesToLowerCaseWithUnderscore;

/**
 * Handles all alias namings which is used for database tables relations of java entities.
 * As well as other naming conventions such as plural and singulars.
 */
@Slf4j
public class NamingService {

  /**
   * The standard alias naming in SQL queries in relation to the table name.
   * @param tableName The name of the table that will have it's alias calculated
   * @return The defined alias of the table's name.
   *         In case that the table name is null or empty, it will return empty string.
   */
  public static String toTableAlias(String tableName) {
    if (tableName == null || tableName.isEmpty()) {
      log.warn(
          "Table name is {} when creating alias, which it should not be!",
          tableName == null ? "null" : "empty"
      );
      return "";
    }

    boolean underscoreReached = false;
    String alias = tableName;

    for (int i = 0; i < alias.length(); i++) {
      char character = alias.charAt(i);
      if (character == '_') {
        underscoreReached = true;
        alias = alias.substring(0, i) + alias.substring(i + 1);
        i -= 1;
      } else if (underscoreReached) {
        alias = alias.substring(0, i) + String.valueOf(character).toUpperCase() + alias.substring(i + 1);
        underscoreReached = false;
      }
    }

    if (alias.equals(tableName)) {
      char[] chars = alias.toCharArray();
      chars[0] = Character.toLowerCase(chars[0]);
      alias = "_" + new String(chars);
    }

    return alias;
  }

  public static String toTableTitle(Class<?> clazz) {
    return upperCasesToLowerCaseWithUnderscore(firstCharacterAsLowercase(clazz.getSimpleName()));
  }

  private static String prepareColumnName(String columnName) {
    String preparation = upperCasesToLowerCaseWithUnderscore(columnName);
    return columnName.charAt(0) == '_'
        ? preparation.substring(1)
        : preparation;
  }

  public static String singularToPlural(String title) {
    if (title == null || (title.endsWith("s") && !title.endsWith("ss")) || title.endsWith("info") || title.endsWith("data"))
      return title;

    return stating(title.endsWith("y"))
        .then(title.substring(0, title.length() - 1) + "ies")
        .or(EternaryService.Operator.Property.inCase(title.endsWith("ss")).then(title + "es"))
        .orElse(title + "s");
  }

  public static String pluralToSingular(String title) {
    if (title == null || title.length() < 2)
      throw new IllegalArgumentException("Entity title is empty when trying to make it singular");

    String ending = title.substring(title.length() - 3);
    return stating(title.length() > 4 && ending.equals("ies"))
        .then(title.substring(0, title.length() - 3) + "y")
        .or(EternaryService.Operator.Property.inCase(ending.endsWith("ses")).then(title.substring(0, title.length() - 2)))
        .orElse(() -> stating(ending.endsWith("s"))
            .then(title.substring(0, title.length() - 1))
            .orElse(title)
        );
  }

  public static String toColumnTitle(Member member) {
    return handleDefineTitle(intendedNameOf(member), false);
  }

  public static String toColumnTitle(IdReferenceMember member) {
    return handleDefineTitle(intendedNameOf(member), false);
  }

  protected static String handleDefineTitle(String title, boolean isTable) {
    if (title == null || title.isEmpty())
      return "";

    title = title.replaceAll("([A-Z])", "_$1");

    for (char c : title.toCharArray()) {
      if (c == '_')
        title = title.substring(1);
      else
        break;
    }

    title = title.toLowerCase();

    return isTable ? singularToPlural(title) : title;
  }
}
