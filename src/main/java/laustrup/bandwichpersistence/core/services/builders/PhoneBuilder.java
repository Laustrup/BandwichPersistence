package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.persistence.Field;

import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.ConvertingService.of;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class PhoneBuilder extends BuilderService<ContactInfo.Phone> {

    private static final Logger _logger = Logger.getLogger(PhoneBuilder.class.getName());

    private static PhoneBuilder _instance;

    public static PhoneBuilder get_instance() {
        if (_instance == null)
            _instance = new PhoneBuilder();

        return _instance;
    }

    private PhoneBuilder() {
        super(ContactInfo.Phone.class, _logger);
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
                    primary -> !getLong(table.apply(Model.ModelDTO.Fields.id).get_content()).equals(primary),
                    numbers
            );

            return new ContactInfo.Phone(
                    of(firstDigits.get()),
                    of(numbers.get()),
                    of(mobile.get()),
                    of(business.get())
            );
        };
    }
}
