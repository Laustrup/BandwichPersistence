package laustrup.bandwichpersistence.core.persistence.models;

import java.lang.reflect.Field;
import java.util.Map;

public class SimpleDatabaseConfigurations extends CommonDatabaseConfigurations {

    private final String _explicitIdReference;

    public SimpleDatabaseConfigurations(String idReference) {
        _explicitIdReference = idReference;
    }

    @Override
    public String explicitIdReference() {
        return _explicitIdReference;
    }

    @Override
    public Map<Field, String> get_explicits() {
        return super.get_explicits();
    }
}
