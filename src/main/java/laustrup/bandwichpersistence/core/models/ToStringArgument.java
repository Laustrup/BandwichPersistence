package laustrup.bandwichpersistence.core.models;

import laustrup.bandwichpersistence.core.utilities.collections.Collection;
import lombok.Getter;

@Getter
public class ToStringArgument {

    private String _key, _value;

    public ToStringArgument(String key, String value) {
        _key = key;
        _value = value;
    }

    public static String[][] convert(Collection<ToStringArgument> arguments) {
        String[][] content = new String[arguments.get_data().length][];

        for (int i = 0; i < arguments.get_data().length; i++) {
            ToStringArgument argument = arguments.get_data()[i];
            content[i] = new String[]{argument.get_key(), argument.get_value()};
        }

        return content;
    }
}
