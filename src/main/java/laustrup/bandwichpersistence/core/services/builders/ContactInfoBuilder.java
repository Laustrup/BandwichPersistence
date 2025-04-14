package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
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

    private final PhoneBuilder _phoneBuilder = PhoneBuilder.get_instance();

    private final AddressBuilder _addressBuilder = AddressBuilder.get_instance();

    private final CountryBuilder _countryBuilder = CountryBuilder.get_instance();

    public static ContactInfoBuilder get_instance() {
        if (_instance == null)
            _instance = new ContactInfoBuilder();

        return _instance;
    }

    private ContactInfoBuilder() {
        super(_instance, _logger);
    }

    @Override
    protected void completion(ContactInfo reference, ContactInfo object) {
        combine(reference.get_phones(), object.get_phones());
    }

    @Override
    protected Function<Function<String, JDBCService.Field>, ContactInfo> logic(ResultSet resultSet) {
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
                        combine(phones, _phoneBuilder.build(resultSet));
                        _addressBuilder.complete(address, _addressBuilder.build(resultSet));
                        _countryBuilder.complete(country, _countryBuilder.build(resultSet));
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
