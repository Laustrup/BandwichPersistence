package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class ContactInfoBuilder extends BuilderService<ContactInfo> {

    private static final Logger _logger = Logger.getLogger(ContactInfoBuilder.class.getSimpleName());

    private static ContactInfoBuilder _instance;

    public static ContactInfoBuilder get_instance() {
        if (_instance == null)
            _instance = new ContactInfoBuilder();

        return _instance;
    }

    private ContactInfoBuilder() {
        super(ContactInfo.class, ContactInfo.class::getSimpleName, _logger);
    }

    @Override
    protected void completion(ContactInfo reference, ContactInfo object) {
        combine(reference.get_phones(), object.get_phones());
    }

    @Override
    protected Function<Function<String, Field>, ContactInfo> logic(ResultSet resultSet) {
        return table -> {
            AtomicReference<UUID> id = new AtomicReference<>();
            AtomicReference<String> email = new AtomicReference<>();
            Seszt<ContactInfo.Phone> phones = new Seszt<>();
            AtomicReference<ContactInfo.Address> address = new AtomicReference<>();
            AtomicReference<ContactInfo.Country> country = new AtomicReference<>();

            interaction(
                    resultSet,
                    () -> {
                        set(id, table.apply(Model.ModelDTO.Fields.id));
                        set(email, table.apply(ContactInfo.DTO.Fields.email));
                        combine(phones, PhoneBuilder.get_instance().build(resultSet));
                        AddressBuilder.get_instance().complete(address, AddressBuilder.get_instance().build(resultSet));
                        CountryBuilder.get_instance().complete(country, CountryBuilder.get_instance().build(resultSet));
                    },
                    id
            );

            return new ContactInfo(
                    id.get(),
                    email.get(),
                    phones,
                    address.get(),
                    country.get()
            );
        };
    }
}
