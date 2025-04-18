package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.users.Artist;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;

import java.sql.ResultSet;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static laustrup.bandwichpersistence.core.models.Subscription.UserType.valueOf;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class UserBuilder extends BuilderService<User> {

    private static final Logger _logger = Logger.getLogger(UserBuilder.class.getName());

    private static UserBuilder _instance;

    public static UserBuilder get_instance() {
        if (_instance == null)
            _instance = new UserBuilder();

        return _instance;
    }

    private UserBuilder() {
        super(User.class, _logger);
    }

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

    @Override
    public User build(ResultSet resultSet) {
        return switch (peek(
                resultSet,
                () -> valueOf(getString(Subscription.DTO.Fields.userType)),
                exception -> _logger.warning("Couldn't find user type when building user!\n" + exception.getMessage())
        )) {
            case ARTIST -> ArtistBuilder.get_instance().build(resultSet);
            case ORGANISATION_EMPLOYEE -> OrganisationEmployeeBuilder.get_instance().build(resultSet);
            case null -> null;
            default -> throw new IllegalStateException("The type of User to build couldn't be found!");
        };
    }

    @Override
    protected void completion(User collective, User part) {
        switch (collective.get_subscription().get_userType()) {
            case ARTIST -> ArtistBuilder.get_instance().completion((Artist) collective, (Artist) part);
            case ORGANISATION_EMPLOYEE -> OrganisationEmployeeBuilder.get_instance().completion((Organisation.Employee) collective, (Organisation.Employee) part);
            default -> throw new IllegalStateException("The type of User to build couldn't be found!");
        }
    }

    @Override
    protected Function<Function<String, Field>, User> logic(ResultSet resultSet) {
        throw new UnsupportedOperationException();
    }
}
