package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.users.Artist;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;

import java.sql.ResultSet;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static laustrup.bandwichpersistence.core.models.Subscription.UserType.valueOf;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class UserBuilder extends BuilderService<User> {

    private final ArtistBuilder _artistBuilder = new ArtistBuilder();

    private final OrganisationEmployeeBuilder _organisationEmployeeBuilder = new OrganisationEmployeeBuilder();

    public UserBuilder() {
        super(User.class, UserBuilder.class);
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
            case ARTIST -> _artistBuilder.build(resultSet);
            case ORGANISATION_EMPLOYEE -> _organisationEmployeeBuilder.build(resultSet);
            case null -> null;
            default -> throw new IllegalStateException("The type of User to build couldn't be found!");
        };
    }

    @Override
    protected void completion(User collective, User part) {
        switch (collective.get_subscription().get_userType()) {
            case ARTIST -> _artistBuilder.completion((Artist) collective, (Artist) part);
            case ORGANISATION_EMPLOYEE -> _organisationEmployeeBuilder.completion((Organisation.Employee) collective, (Organisation.Employee) part);
            default -> throw new IllegalStateException("The type of User to build couldn't be found!");
        }
    }

    @Override
    protected Function<Function<String, Field>, User> logic(ResultSet resultSet) {
        throw new UnsupportedOperationException();
    }
}
