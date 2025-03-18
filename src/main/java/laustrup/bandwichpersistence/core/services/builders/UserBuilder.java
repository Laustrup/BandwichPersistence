package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.services.UserService;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class UserBuilder {

    private static final Logger _logger = Logger.getLogger(UserBuilder.class.getSimpleName());

    public static Stream<Login> buildLogins(ResultSet resultSet) {
        List<Login> logins = new ArrayList<>();

        JDBCService.build(
                resultSet,
                () -> logins.add(new Login(
                        get(
                                column -> getString(resultSet, column),
                                Login.Fields.password
                        ),
                        UserService.from(Objects.requireNonNull(build(resultSet)))
                ))
        );

        _logger.info("Successfully built logins");

        return logins.stream();
    }

    public static User build(ResultSet resultSet) {
        Subscription.UserType userType;

        try {
            if (resultSet.next())
                userType = Subscription.UserType.valueOf(getString(
                        resultSet,
                        Subscription.DTO.Fields.userType
                ));
            else
                return null;

            resultSet.previous();
        } catch (SQLException e) {
            _logger.warning("Couldn't find user type when building user!\n" + e.getMessage());
            return null;
        }

        return switch (userType) {
            case ARTIST -> ArtistBuilder.build(resultSet);
            case ORGANISATION_EMPLOYEE -> OrganisationBuilder.buildEmployee(resultSet);
            default -> throw new IllegalStateException();
        };
    }

    public static Subscription buildSubscription(ResultSet resultSet) {
        AtomicReference<UUID> id = new AtomicReference<>();
        AtomicReference<Subscription.Status> status = new AtomicReference<>();
        AtomicReference< Subscription.Kind> kind = new AtomicReference<>();
        AtomicReference< Subscription.UserType> userType = new AtomicReference<>();

        try {
            JDBCService.build(
                    resultSet,
                    () -> {
                        id.set(get(
                                column -> getUUID(resultSet, column),
                                Subscription.DTO.Fields.id
                        ));
                        status.set(get(
                                column -> Subscription.Status.valueOf(getString(resultSet, column)),
                                Subscription.DTO.Fields.status
                        ));
                        kind.set(get(
                                column -> Subscription.Kind.valueOf(getString(resultSet, column)),
                                Subscription.DTO.Fields.kind
                        ));
                        userType.set(get(
                                column -> Subscription.UserType.valueOf(getString(resultSet, column)),
                                Subscription.DTO.Fields.userType
                        ));
                    },
                    primary -> !get(
                            column -> getUUID(resultSet, column),
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
                        id.set(get(
                                column -> getUUID(resultSet, column),
                                Model.ModelDTO.Fields.id
                        ));
                        email.set(get(
                                column -> getString(resultSet, column),
                                ContactInfo.DTO.Fields.email
                        ));
                        phones.add(buildPhone(resultSet));
                        address.set(buildAddress(resultSet));
                        country.set(buildCountry(resultSet));
                    },
                    primary -> !get(
                            column -> getUUID(resultSet, column),
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
        AtomicReference<ContactInfo.Country> country = new AtomicReference<>();
        AtomicReference<Long> numbers = new AtomicReference<>();
        AtomicReference<Boolean> mobile = new AtomicReference<>();
        AtomicReference<Boolean> business = new AtomicReference<>();

        try {
            JDBCService.build(
                    resultSet,
                    () -> {
                        country.set(buildCountry(resultSet));
                        numbers.set(get(
                                column -> getLong(resultSet, column),
                                ContactInfo.Phone.DTO.Fields.numbers
                        ));
                        mobile.set(get(
                                column -> getBoolean(resultSet, column),
                                ContactInfo.Phone.DTO.Fields.mobile
                        ));
                        business.set(get(
                                column -> getBoolean(resultSet, column),
                                ContactInfo.Phone.DTO.Fields.business
                        ));
                    },
                    primary -> !get(
                            column -> getLong(resultSet, column),
                            Model.ModelDTO.Fields.id
                    ).equals(primary),
                    numbers.get()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new ContactInfo.Phone(
                country.get(),
                numbers.get(),
                mobile.get(),
                business.get()
        );
    }

    public static ContactInfo.Address buildAddress(ResultSet resultSet) {

    }

    public static ContactInfo.Country buildCountry(ResultSet resultSet) {

    }
}
