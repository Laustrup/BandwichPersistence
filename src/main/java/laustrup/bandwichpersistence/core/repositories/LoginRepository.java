package laustrup.bandwichpersistence.core.repositories;

import laustrup.bandwichpersistence.core.models.Login;
import laustrup.bandwichpersistence.core.persistence.DatabaseManager;
import laustrup.bandwichpersistence.core.persistence.DatabaseParameter;
import laustrup.bandwichpersistence.core.repositories.queries.LoginQuery;
import laustrup.bandwichpersistence.core.repositories.queries.LoginQuery.Parameter;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.lists.Liszt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

public class LoginRepository {

    public static ResultSet getUser(Login login) {
        try {
            return DatabaseManager.read(
                    LoginQuery.selectAll,
                    Stream.of(
                            new DatabaseParameter(
                                    Parameter.CONTACT_INFO_EMAIL.get_key(),
                                    login.getUser().getContactInfo().getEmail()
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
