package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Event;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.persistence.Field;

import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class CountryBuilder extends BuilderService<ContactInfo.Country> {

    private static final Logger _logger = Logger.getLogger(CountryBuilder.class.getName());

    private static CountryBuilder _instance;

    public static CountryBuilder get_instance() {
        if (_instance == null)
            _instance = new CountryBuilder();

        return _instance;
    }

    private CountryBuilder() {
        super(ContactInfo.Country.class, _logger);
    }

    @Override
    public ContactInfo.Country build(ResultSet resultSet) {
        return handle(logic(resultSet));
    }

    @Override
    protected void completion(ContactInfo.Country reference, ContactInfo.Country object) {

    }

    @Override
    protected Function<Function<String, Field>, ContactInfo.Country> logic(ResultSet resultSet) {
        return table -> {
            AtomicReference<UUID> id = new AtomicReference<>();
            AtomicReference<String> title = new AtomicReference<>();
            AtomicReference<String> code = new AtomicReference<>();

            interaction(
                    resultSet,
                    () -> {
                        set(id, table.apply(ContactInfo.Country.DTO.Fields.id));
                        set(title, table.apply(ContactInfo.Country.DTO.Fields.title));
                        set(code, table.apply(ContactInfo.Country.DTO.Fields.code));
                    },
                    id
            );

            return new ContactInfo.Country(
                    id.get(),
                    title.get(),
                    code.get()
            );
        };
    }
}
