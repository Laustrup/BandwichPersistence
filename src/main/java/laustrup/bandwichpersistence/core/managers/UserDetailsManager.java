package laustrup.bandwichpersistence.core.managers;

import laustrup.bandwichpersistence.core.models.Login;
import laustrup.bandwichpersistence.core.models.Response;
import laustrup.bandwichpersistence.core.models.Subscription;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.repositories.UserDetailsRepository;
import laustrup.bandwichpersistence.core.services.builders.UserBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.ResultSet;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.managers.ManagerService.databaseInteraction;
import static laustrup.bandwichpersistence.core.services.PasswordService.matches;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.ResultSetService.*;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.ResultSetService.Configurations.Mode.*;

public class UserDetailsManager {

    private static final Logger _logger = Logger.getLogger(UserDetailsManager.class.getName());

    static private final UserBuilder _userBuilder = UserBuilder.get_instance();

    public static UserDetails getUserDetails(String email) {
        return databaseInteraction(() ->
                UserBuilder.buildLogins(UserDetailsRepository.getUserDetailsByEmail(email))
                        .findFirst()
                        .orElse(null)
        );
    }

    public static Response<User> getUser(Login login) {
        return databaseInteraction(() -> {
            User user = null;
            HttpStatus status = HttpStatus.OK;

            try {
                user = _userBuilder.build(passwordFits(
                        UserDetailsRepository.getUserByEmail(login),
                        login.getPassword()
                ));
            } catch (IllegalArgumentException exception) {
                status = HttpStatus.UNAUTHORIZED;
                _logger.warning(exception.getMessage());
            } catch (Exception exception) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                _logger.warning(exception.getMessage());
            }

            return new Response<>(user, status);
        });
    }

    private static ResultSet passwordFits(ResultSet resultSet, String password) {
        if (matches(password, get(
                new ResultSetService.Configurations(new Field(getUserType(resultSet), "password"), resultSet, PEEK),
                String.class
        )))
            return resultSet;
        else
            throw new IllegalArgumentException("password does not match");
    }

    public static String getUserType(ResultSet resultSet) {
        return get(
                new Configurations(
                        Field.of(
                                Subscription.class.getSimpleName() + "s",
                                Subscription.DTO.Fields.userType
                        ),
                        resultSet,
                        PEEK
                ),
                String.class
        );
    }
}
