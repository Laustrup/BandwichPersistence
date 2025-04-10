package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService.Field;

import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class PhoneBuilder extends BuilderService<ContactInfo.Phone> {

    public PhoneBuilder() {
        super(ContactInfo.Phone.class, PhoneBuilder.class);
    }

    @Override
    protected void completion(ContactInfo.Phone reference, ContactInfo.Phone object) {

    }

    @Override
    protected Function<Function<String, Field>, ContactInfo.Phone> logic(ResultSet resultSet) {
        return table -> {
            AtomicReference<Integer> firstDigits = new AtomicReference<>();
            AtomicReference<Long> numbers = new AtomicReference<>();
            AtomicReference<Boolean> mobile = new AtomicReference<>();
            AtomicReference<Boolean> business = new AtomicReference<>();

            interaction(
                    resultSet,
                    () -> {
                        set(firstDigits, table.apply(ContactInfo.Phone.DTO.Fields.countryDigits));
                        set(numbers, table.apply(ContactInfo.Phone.DTO.Fields.numbers));
                        set(mobile, table.apply(ContactInfo.Phone.DTO.Fields.isMobile));
                        set(business, table.apply(ContactInfo.Phone.DTO.Fields.isBusiness));
                    },
                    primary -> !getLong(Model.ModelDTO.Fields.id).equals(primary),
                    numbers
            );

            return new ContactInfo.Phone(
                    firstDigits.get(),
                    numbers.get(),
                    mobile.get(),
                    business.get()
            );
        };
    }
}
