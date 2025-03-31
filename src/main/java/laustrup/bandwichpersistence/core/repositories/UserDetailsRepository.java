package laustrup.bandwichpersistence.core.repositories;

import laustrup.bandwichpersistence.core.models.Login;
import laustrup.bandwichpersistence.core.persistence.DatabaseManager;
import laustrup.bandwichpersistence.core.persistence.DatabaseParameter;
import laustrup.bandwichpersistence.core.repositories.queries.UserDetailsQueries;
import laustrup.bandwichpersistence.core.repositories.queries.UserDetailsQueries.Parameter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

public class UserDetailsRepository {

    public static ResultSet getUserDetails() {
        return DatabaseManager.read(UserDetailsQueries.selectAllForLogins);
    }

    public static ResultSet getUserDetailsByEmail(String email) {
        return DatabaseManager.read(
                UserDetailsQueries.selectAUserDetails,
                new DatabaseParameter(Parameter.CONTACT_INFO_EMAIL.get_key(), email)
        );
    }

    public static ResultSet getUser(Login login) {
        try {
            return DatabaseManager.read(
                    UserDetailsQueries.selectAllForLogin,
                    Stream.of(
                            new DatabaseParameter(
                                    Parameter.CONTACT_INFO_EMAIL.get_key(),
                                    login.getUsername()
                            ),
                            new DatabaseParameter(
                                    Parameter.PASSWORD.get_key(),
                                    login.getPassword()
                            )
                    )
            ).getResultSet();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
