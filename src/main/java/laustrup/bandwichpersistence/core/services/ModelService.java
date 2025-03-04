package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.models.Event;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.utilities.console.Printer;
import lombok.Getter;

import java.util.UUID;

public class ModelService {

    /** For the defineToString of how it should be split. */
    @Getter
    private static final String
            _toStringFieldSplitter = ",\n \t",
            _toStringKeyValueSplitter = ":\t";

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
                    content.append(keys[i]).append(_toStringKeyValueSplitter).append(values[i] != null ? values[i] : "null");
                    if (i < keys.length-1)
                        content.append(_toStringFieldSplitter);
                }
            else
                content = new StringBuilder("Content couldn't be generated, since there are less attributes than values");
        } catch (Exception e) {
            String message = title + " had an error when trying to define its ToString.";
            Printer.print(message, e);
            content = new StringBuilder(primaryId != null ? String.valueOf(primaryId) : message);
            content.append(secondaryId != null ? String.valueOf(secondaryId) : message);
        }

        return title + "(\n \t" + content + "\n)";
    }
}
