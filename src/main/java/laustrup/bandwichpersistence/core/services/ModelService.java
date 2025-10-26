package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.models.Event;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.identification.Identity;
import laustrup.bandwichpersistence.core.models.identification.Signature;
import laustrup.bandwichpersistence.core.models.users.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import static laustrup.bandwichpersistence.core.services.ObjectService.ifTrue;

@Slf4j
public class ModelService {

  /**
   * For the defineToString of how it should be split.
   */
  @Getter
  private static final String
      _toStringFieldSplitter = ",\n\t",
      _toStringKeyValueSplitter = ": ";

  public static <IDENTITY extends Identity<SIGNATURE>, SIGNATURE extends Signature<UUID>> Model<IDENTITY, SIGNATURE> from(
      Model.ModelDTO<IDENTITY, SIGNATURE, UUID> model
  ) {
    return ifTrue(model.getClass() == Event.DTO.class,
        new Event((Event.DTO) model),
        UserService.from((User.UserDTO) model)
    );
  }

  @SuppressWarnings("unchecked")
  public static <IDENTITY extends Identity<SIGNATURE>, SIGNATURE extends Signature<?>> Model.ModelDTO<IDENTITY, SIGNATURE, ?> from(
      Model<IDENTITY, SIGNATURE> model
  ) {
    return ifTrue(model.getClass() == Event.class,
        new Event.DTO((Event) model),
        UserService.from((User) model)
    );
  }

  public static <SIGNATURE extends Signature<?>> String defineToString(
      String title,
      Identity<SIGNATURE> id,
      String[] keys,
      String[] values
  ) {
    return defineToString(title, id, null, keys, values);
  }

  public static <SIGNATURE extends Signature<?>> String defineToString(
      String title,
      Identity<SIGNATURE> primaryId,
      Identity<SIGNATURE> secondaryId,
      String[] keys,
      String[] values
  ) {
    StringBuilder content = new StringBuilder();

    try {
      if (values.length <= keys.length)
        for (int i = 0; i < keys.length; i++) {
          content.append(keys[i])
              .append(_toStringKeyValueSplitter)
              .append(values[i] != null ? values[i] : "null");
          if (i < keys.length - 1)
            content.append(_toStringFieldSplitter);
        }
      else
        throw new IllegalArgumentException("Content couldn't be generated, since there are less attributes than values");
    } catch (Exception e) {
      String message = title + " had an error when trying to define its ToString.";
      log.error(message, e);
      content = new StringBuilder(primaryId != null ? String.valueOf(primaryId) : message);
      content.append(secondaryId != null ? String.valueOf(secondaryId) : message);
    }

    return title + "(\n \t" + content + "\n)";
  }

  public static <IDENTITY extends Identity<SIGNATURE>, SIGNATURE extends Signature<?>> Identity.Identifier<Signature<UUID>> getId(Model<IDENTITY, SIGNATURE> model) {
    return getId(model.toString());
  }

  public static <IDENTITY extends Identity<SIGNATURE>, SIGNATURE extends Signature<?>> Stream<Identity.Identifier<Signature<UUID>>> getIds(Model<IDENTITY, SIGNATURE> model) {
    return getIds(model.toString());
  }

  public static Identity.Identifier<Signature<UUID>> getId(String toString) {
    return handleGetIds(toString, false)
        .findFirst()
        .orElse(null);
  }

  public static Stream<Identity.Identifier<Signature<UUID>>> getIds(String toString) {
    return handleGetIds(toString, true);
  }

  public static Stream<Identity.Identifier<Signature<UUID>>> handleGetIds(String toString, boolean isPlural) {
    if (toString == null || toString.contains("identity=null,"))
      return Stream.empty();

    boolean isValue = false;
    StringBuilder
        store = new StringBuilder(),
        value = new StringBuilder();
    String separator = "\\|";

    Function<String, String> substring = splitting -> {
      int from = store.length() - splitting.length();
      return store.toString().length() > from && from > 0 ? store.substring(from) : "";
    };

    for (char character : toString.toCharArray()) {
      store.append(character);
      if (!isValue && substring.apply(_toStringKeyValueSplitter).equals(_toStringKeyValueSplitter)) {
        isValue = true;
        continue;
      }
      if (isValue && substring.apply(_toStringFieldSplitter).equals(_toStringFieldSplitter) || character == '\n') {
        isValue = false;
        if (!isPlural)
          break;
      }

      if (isValue)
        value.append(character);
    }

    return Arrays.stream(value.toString()
        .split(separator))
        .map(string -> Identity.Identifier.of(Signature.UUID.fromString(string)));
  }

  public static boolean equals(Object object, Object other) {
    List<Identity.Identifier<Signature<UUID>>>
        objectIds = getIds(object.toString()).toList(),
        otherIds = getIds(other.toString()).toList();

    return objectIds.stream()
        .allMatch(id -> otherIds.stream()
            .anyMatch(identity -> otherIds.stream()
                .anyMatch(otherId -> otherId.get_signature().get_value().equals(id.get_signature().get_value()))
            )
        );
  }
}
