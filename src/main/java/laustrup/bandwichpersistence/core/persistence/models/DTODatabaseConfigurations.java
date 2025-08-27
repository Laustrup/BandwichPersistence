package laustrup.bandwichpersistence.core.persistence.models;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DTODatabaseConfigurations extends CommonDatabaseConfigurations {

    private final String _explicitIdReference;
    private final Map<Field, String> _explicits;

    public DTODatabaseConfigurations(String explicitIdReference, Map<Field, String> explicits) {
        _explicitIdReference = explicitIdReference;
        _explicits = explicits;
    }

    public DTODatabaseConfigurations(String _explicitIdReference) {
        this(_explicitIdReference, new HashMap<>());
    }

    public DTODatabaseConfigurations(DatabaseEntityConfigurations entityConfigurations) {
        this(entityConfigurations.explicitIdReference(), entityConfigurations.get_explicits());
    }

    @Override
    public String explicitIdReference() {
        return _explicitIdReference;
    }

    @Override
    public Map<Field, String> get_explicits() {
        return _explicits;
    }
}
