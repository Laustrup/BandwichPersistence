package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Login;
import laustrup.bandwichpersistence.core.models.Organisation.Employee;
import laustrup.bandwichpersistence.core.models.Subscription.UserType;
import laustrup.bandwichpersistence.core.models.users.Artist;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.models.users.User;
import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static laustrup.bandwichpersistence.core.managers.UserDetailsManager.getUserType;
import static laustrup.bandwichpersistence.core.services.ClassFieldService.getDeclared;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.getString;

public class UserBuilder {

    private static final ArtistBuilder _artistBuilder = ArtistBuilder.get_instance();

    private static final OrganisationEmployeeBuilder _organisationEmployeeBuilder = OrganisationEmployeeBuilder.get_instance();

    private static UserBuilder _instance;

    public static UserBuilder get_instance() {
        if (_instance == null)
            _instance = new UserBuilder();

        return _instance;
    }

    private UserBuilder() {

    }

    public static Stream<Login> buildLogins(ResultSet resultSet) {
        List<Login> logins = new ArrayList<>();

        JDBCService.build(
                resultSet,
                () -> logins.add(new Login(
                        getString(DatabaseField.of(getDeclared(Login.class, Login.Fields.password))),
                        getString(DatabaseField.of(getDeclared(ContactInfo.class, ContactInfo.DTO.Fields.email)))
                ))
        );

        return logins.stream();
    }

    public User<? extends User.Id> build(ResultSet resultSet) {
        Optional<String> usertype = Optional.ofNullable(getUserType(resultSet));

        if (usertype.isEmpty())
            return null;

        return switch (UserType.valueOf(usertype.orElseThrow().toUpperCase())) {
            case ARTIST -> _artistBuilder.build(resultSet);
            case ORGANISATION_EMPLOYEE -> _organisationEmployeeBuilder.build(resultSet);
            default -> throw new IllegalStateException("The type of User to build couldn't be found!");
        };
    }

    protected void completion(User<? extends User.Id> collective, User<? extends User.Id> part) {
        switch (collective.get_subscription().get_userType()) {
            case ARTIST -> _artistBuilder.completion((Artist) collective, (Artist) part);
            case ORGANISATION_EMPLOYEE -> _organisationEmployeeBuilder.completion(
                    (Employee) collective,
                    (Employee) part
            );
            default -> throw new IllegalStateException("The type of User to build couldn't be found!");
        }
    }
}
