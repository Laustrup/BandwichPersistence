package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class ContactInfoBuilder extends BuilderService<ContactInfo> {

    private final PhoneBuilder _phoneBuilder = new PhoneBuilder();

    private final AddressBuilder _addressBuilder = new AddressBuilder();

    private final CountryBuilder _countryBuilder = new CountryBuilder();

    public ContactInfoBuilder() {
        super(ContactInfo.class, ContactInfoBuilder.class);
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
