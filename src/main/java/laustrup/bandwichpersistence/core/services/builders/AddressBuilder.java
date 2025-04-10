package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;

import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class AddressBuilder extends BuilderService<ContactInfo.Address> {

    public AddressBuilder() {
        super(ContactInfo.Address.class, AddressBuilder.class);
    }

    @Override
    protected void completion(ContactInfo.Address reference, ContactInfo.Address object) {

    }

    @Override
    protected Function<Function<String, Field>, ContactInfo.Address> logic(ResultSet resultSet) {
        return table -> {
            AtomicReference<UUID> id = new AtomicReference<>();

            AtomicReference<String>
                    street = new AtomicReference<>(),
                    floor = new AtomicReference<>(),
                    municipality = new AtomicReference<>(),
                    zip = new AtomicReference<>(),
                    city = new AtomicReference<>();

            interaction(
                    resultSet,
                    () -> {
                        set(id, table.apply(Model.ModelDTO.Fields.id));
                        set(street, table.apply(ContactInfo.Address.DTO.Fields.street));
                        set(floor, table.apply(ContactInfo.Address.DTO.Fields.floor));
                        set(municipality, table.apply(ContactInfo.Address.DTO.Fields.municipality));
                        set(zip, table.apply(ContactInfo.Address.DTO.Fields.zip));
                        set(city, table.apply(ContactInfo.Address.DTO.Fields.city));
                    },
                    id
            );

            return new ContactInfo.Address(
                    id.get(),
                    street.get(),
                    floor.get(),
                    municipality.get(),
                    zip.get(),
                    city.get()
            );
        };
    }
}
