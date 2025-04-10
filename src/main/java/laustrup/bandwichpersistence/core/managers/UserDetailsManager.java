package laustrup.bandwichpersistence.core.managers;

import laustrup.bandwichpersistence.core.models.Login;
import laustrup.bandwichpersistence.core.models.Response;
import laustrup.bandwichpersistence.core.models.Subscription;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.repositories.UserDetailsRepository;
import laustrup.bandwichpersistence.core.services.builders.UserBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.ResultSet;
import java.util.Objects;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.managers.ManagerService.databaseInteraction;
import static laustrup.bandwichpersistence.core.services.PasswordService.matches;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class UserDetailsManager {

    private static final Logger _logger = Logger.getLogger(UserDetailsManager.class.getName());

    private static final UserBuilder _userBuilder = new UserBuilder();

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
                exception.printStackTrace();
            } catch (Exception exception) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                _logger.warning(exception.getMessage());
                exception.printStackTrace();
            }

            return new Response<>(user, status);
        });
    }

    private static ResultSet passwordFits(ResultSet resultSet, String password) {
        if (matches(
                password,
                peek(
                        resultSet,
                        results ->
                                get(Field.of(Objects.requireNonNull(get(
                                                        Field.of(
                                                                Subscription.class.getSimpleName() + "s",
                                                                Subscription.DTO.Fields.userType
                                                        ),
                                                        String.class
                                                )).toLowerCase() + "s",
                                                Login.Fields.password
                                        ),
                                        String.class
                                ),
                        exception -> _logger.warning("Issue when checking for password fit:\n\n" + exception.getMessage())
                )
        ))
            return resultSet;
        else
            throw new IllegalArgumentException("password does not match");
    }
}
