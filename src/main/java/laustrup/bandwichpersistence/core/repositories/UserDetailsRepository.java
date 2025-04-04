package laustrup.bandwichpersistence.core.repositories;

import laustrup.bandwichpersistence.core.models.Login;
import laustrup.bandwichpersistence.core.persistence.DatabaseParameter;
import laustrup.bandwichpersistence.core.repositories.queries.UserDetailsQueries;
import laustrup.bandwichpersistence.core.repositories.queries.UserDetailsQueries.Parameter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static laustrup.bandwichpersistence.core.persistence.DatabaseManager.read;

public class UserDetailsRepository {

    public static ResultSet getUserDetails() {
        return read(UserDetailsQueries.selectAllForLogins).get_resultSet();
    }

    public static ResultSet getUserDetailsByEmail(String email) {
        return read(
                UserDetailsQueries.selectAllForLogin,
                new DatabaseParameter(Parameter.CONTACT_INFO_EMAIL.get_key(), email)
        ).get_resultSet();
    }

    public static ResultSet getUserByEmail(Login login) {
        return read(
                UserDetailsQueries.selectAllForLogin,
                Stream.of(
                        new DatabaseParameter(
                                Parameter.CONTACT_INFO_EMAIL.get_key(),
                                login.getUsername()
                        )
                )
        ).get_resultSet();
    }
}
