package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static laustrup.bandwichpersistence.core.models.Subscription.UserType.valueOf;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class UserBuilder {

    private static final Logger _logger = Logger.getLogger(UserBuilder.class.getSimpleName());

    public static Stream<Login> buildLogins(ResultSet resultSet) {
        List<Login> logins = new ArrayList<>();

        JDBCService.build(
                resultSet,
                () -> logins.add(new Login(
                        getString(Login.Fields.password),
                        getString(ContactInfo.DTO.Fields.email)
                ))
        );

        return logins.stream();
    }

    public static User build(ResultSet resultSet) {
        return switch (peek(
                resultSet,
                () -> valueOf(getString(Subscription.DTO.Fields.userType)),
                exception -> _logger.warning("Couldn't find user type when building user!\n" + exception.getMessage())
        )) {
            case ARTIST -> ArtistBuilder.build(resultSet);
            case ORGANISATION_EMPLOYEE -> OrganisationBuilder.buildEmployee(resultSet);
            case null -> null;
            default -> throw new IllegalStateException("The type of User to build couldn't be found!");
        };
    }

    public static Subscription buildSubscription(ResultSet resultSet) {
        AtomicReference<UUID> id = new AtomicReference<>();
        AtomicReference<Subscription.Status> status = new AtomicReference<>();
        AtomicReference<Subscription.Kind> kind = new AtomicReference<>();
        AtomicReference<Subscription.UserType> userType = new AtomicReference<>();

        try {
            JDBCService.build(
                    resultSet,
                    () -> {
                        id.set(getUUID(Subscription.DTO.Fields.id));
                        status.set(Subscription.Status.valueOf(getString(Subscription.DTO.Fields.status)));
                        kind.set(Subscription.Kind.valueOf(getString(Subscription.DTO.Fields.kind)));
                        userType.set(valueOf(getString(Subscription.DTO.Fields.userType)));
                    },
                    primary -> !get(
                            JDBCService::getUUID,
                            Model.ModelDTO.Fields.id
                    ).equals(primary),
                    id.get()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new Subscription(
                id.get(),
                status.get(),
                kind.get(),
                userType.get()
        );
    }

    public static ContactInfo buildContactInfo(ResultSet resultSet) {
        AtomicReference<UUID> id = new AtomicReference<>();
        AtomicReference<String> email = new AtomicReference<>();
        Seszt<ContactInfo.Phone> phones = new Seszt<>();
        AtomicReference<ContactInfo.Address> address = new AtomicReference<>();
        AtomicReference<ContactInfo.Country> country = new AtomicReference<>();

        try {
            JDBCService.build(
                    resultSet,
                    () -> {
                        id.set(getUUID(Model.ModelDTO.Fields.id));
                        email.set(getString(ContactInfo.DTO.Fields.email));
                        phones.add(buildPhone(resultSet));
                        address.set(buildAddress(resultSet));
                        country.set(buildCountry(resultSet));
                    },
                    primary -> !get(
                            JDBCService::getUUID,
                            Model.ModelDTO.Fields.id
                    ).equals(primary),
                    id.get()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new ContactInfo(
                id.get(),
                email.get(),
                phones,
                address.get(),
                country.get()
        );
    }

    public static ContactInfo.Phone buildPhone(ResultSet resultSet) {
        AtomicReference<Integer> firstDigits = new AtomicReference<>();
        AtomicReference<Long> numbers = new AtomicReference<>();
        AtomicReference<Boolean> mobile = new AtomicReference<>();
        AtomicReference<Boolean> business = new AtomicReference<>();

        try {
            JDBCService.build(
                    resultSet,
                    () -> {
                        firstDigits.set(getInteger(ContactInfo.Phone.DTO.Fields.countryDigits));
                        numbers.set(getLong(ContactInfo.Phone.DTO.Fields.numbers));
                        mobile.set(getBoolean(ContactInfo.Phone.DTO.Fields.isMobile));
                        business.set(getBoolean(ContactInfo.Phone.DTO.Fields.isBusiness));
                    },
                    primary -> !getLong(Model.ModelDTO.Fields.id).equals(primary),
                    numbers.get()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new ContactInfo.Phone(
                firstDigits.get(),
                numbers.get(),
                mobile.get(),
                business.get()
        );
    }

    public static ContactInfo.Address buildAddress(ResultSet resultSet) {
        AtomicReference<UUID> id = new AtomicReference<>();

        AtomicReference<String>
                street = new AtomicReference<>(),
                floor = new AtomicReference<>(),
                municipality = new AtomicReference<>(),
                zip = new AtomicReference<>(),
                city = new AtomicReference<>();

        try {
            JDBCService.build(
                    resultSet,
                    () -> {
                        id.set(getUUID(Model.ModelDTO.Fields.id));
                        street.set(getString(ContactInfo.Address.DTO.Fields.street));
                        floor.set(getString(ContactInfo.Address.DTO.Fields.floor));
                        municipality.set(getString(ContactInfo.Address.DTO.Fields.municipality));
                        zip.set(getString(ContactInfo.Address.DTO.Fields.zip));
                        city.set(getString(ContactInfo.Address.DTO.Fields.city));
                    },
                    id.get()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new ContactInfo.Address(
                id.get(),
                street.get(),
                floor.get(),
                municipality.get(),
                zip.get(),
                city.get()
        );
    }

    public static ContactInfo.Country buildCountry(ResultSet resultSet) {
        AtomicReference<UUID> id = new AtomicReference<>();
        AtomicReference<String> title = new AtomicReference<>();
        AtomicReference<String> code = new AtomicReference<>();

        try {
            JDBCService.build(
                    resultSet,
                    () -> {
                        id.set(getUUID(ContactInfo.Country.DTO.Fields.id));
                        title.set(getString(ContactInfo.Country.DTO.Fields.title));
                        code.set(getString(ContactInfo.Country.DTO.Fields.code));
                    },
                    id.get()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new ContactInfo.Country(
                id.get(),
                title.get(),
                code.get()
        );
    }
}
