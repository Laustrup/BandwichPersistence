package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.models.Event;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import laustrup.bandwichpersistence.core.utilities.console.Printer;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import static laustrup.bandwichpersistence.core.services.StringService.containsAny;

public class ModelService {

    /** For the defineToString of how it should be split. */
    @Getter
    private static final String
            _toStringFieldSplitter = ",\n\t",
            _toStringKeyValueSplitter = ": ";

    public static Model from(Model.ModelDTO model) {
        return model.getClass() == Event.DTO.class
                ? new Event((Event.DTO) model)
                : UserService.from((User.UserDTO) model);
    }

    public static Model.ModelDTO from(Model model) {
        return model.getClass() == Event.class
                ? new Event.DTO((Event) model)
                : UserService.from((User) model);
    }

    public static String defineToString(String title, UUID id, String[] keys, String[] values) {
        return defineToString(title, id, null, keys, values);
    }

    public static String defineToString(String title, UUID primaryId, UUID secondaryId, String[] keys, String[] values) {
        StringBuilder content = new StringBuilder();

        try {
            if (values.length <= keys.length)
                for (int i = 0; i < keys.length; i++) {
                    content.append(keys[i])
                            .append(_toStringKeyValueSplitter)
                            .append(values[i] != null ? values[i] : "null");
                    if (i < keys.length-1)
                        content.append(_toStringFieldSplitter);
                }
            else
                throw new IllegalArgumentException("Content couldn't be generated, since there are less attributes than values");
        } catch (Exception e) {
            String message = title + " had an error when trying to define its ToString.";
            Printer.print(message, e);
            content = new StringBuilder(primaryId != null ? String.valueOf(primaryId) : message);
            content.append(secondaryId != null ? String.valueOf(secondaryId) : message);
        }

        return title + "(\n \t" + content + "\n)";
    }

    public static UUID getId(Model model) {
        return getId(model.toString());
    }

    public static Stream<UUID> getIds(Model model) {
        return getIds(model.toString());
    }

    public static UUID getId(String toString) {
        return handleGetIds(toString, false)
                .findFirst()
                .orElse(null);
    }

    public static Stream<UUID> getIds(String toString) {
        return handleGetIds(toString, true);
    }

    public static Stream<UUID> handleGetIds(String toString, boolean isPlural) {
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
            if (!isValue && substring.apply(_toStringKeyValueSplitter).equals(_toStringKeyValueSplitter))
                isValue = true;
            if (isValue && substring.apply(_toStringFieldSplitter).equals(_toStringFieldSplitter)) {
                isValue = false;
                value.append(separator);
                if (!isPlural)
                    break;
            }

            if (isValue)
                value.append(character);
        }


        return Arrays.stream(idValues(value.toString())
                .split(separator))
                .map(UUID::fromString);
    }

    private static String idValues(String value) {
        return ifContainsIds(value, string ->
                string.substring(1, string.length() - 1)
                        .replace(",\n\\", "")
                        .replace("\n", "")
                        .replace("| ", "|")
        );
    }

    private static String ifContainsIds(String value, Function<String, String> substring) {
        String idValues = "";

        if (containsAny(value, new Seszt<>(",\n\\")))
            idValues = substring.apply(value);

        return idValues;
    }

    public static boolean equals(Object object, Object other) {
        List<UUID>
                objectIds = getIds(object.toString()).toList(),
                otherIds = getIds(other.toString()).toList();

        return objectIds.stream()
                .allMatch(id -> otherIds.stream().anyMatch(id::equals));
    }
}
