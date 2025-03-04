package laustrup.bandwichpersistence.core.managers;

import jdk.jshell.spi.ExecutionControl;
import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.persistence.DatabaseManager;
import laustrup.bandwichpersistence.core.persistence.DatabaseParameter;
import laustrup.bandwichpersistence.core.persistence.queries.UserQueries;
import laustrup.bandwichpersistence.core.services.PasswordService;
import laustrup.bandwichpersistence.core.services.builders.UserBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.HttpServerErrorException;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static laustrup.bandwichpersistence.core.managers.ManagerService.databaseInteraction;
import static laustrup.bandwichpersistence.core.persistence.queries.UserQueries.Parameter;

public class UserManager {

    private static final Logger _logger = Logger.getLogger(UserManager.class.getSimpleName());

//    public static User get(UUID id) {
//        return databaseInteraction(() -> UserBuilder.build(DatabaseManager.read(
//                UserQueries.getById,
//                new DatabaseParameter(
//                        Parameter.USER_ID.get_key(),
//                        id
//                )
//        )));
//    }

//    public static Response<Login> login(Login login) {
//        return databaseInteraction(() -> {
//                try {
//                    ResultSet resultSet = DatabaseManager.read(
//                            UserQueries.getByEmail,
//                            new DatabaseParameter(
//                                    Parameter.CONTACT_INFO_EMAIL.get_key(),
//                                    login.getUsername()
//                            )
//                    );
//                    User user = UserBuilder.build(resultSet);
//
//                    boolean unacceptable = user != null && !PasswordService.matches(login.getPassword(), user.get_password());
//
//                    return new Response<>(
//                            unacceptable ? null : new Login(
//                                    user != null ? user.get_username() : login.getUsername(),
//                                    user != null ? user.get_password() : login.getPassword(),
//                                    new User.DTO(user)
//                            ),
//                            user == null
//                                    ? Response.Situation.WRONG_USERNAME
//                                    : (unacceptable
//                                            ? Response.Situation.WRONG_PASSWORD
//                                            : Response.Situation.LOGIN_ACCEPTED
//                                    ),
//                            user == null
//                                    ? HttpStatus.FORBIDDEN
//                                    : (unacceptable
//                                            ? HttpStatus.UNAUTHORIZED
//                                            : HttpStatus.OK
//                                    )
//                    );
//                } catch (Exception e) {
//                    _logger.log(
//                            Level.WARNING,
//                            String.format("Issue when logging in with user \"%s\"", login.getUsername()),
//                            e
//                    );
//                    throw new RuntimeException(e);
//                }
//            }
//        );
//    }
//
//    public static Response<User.DTO> upsert(Login login) {
//        return login.getUser() != null
//                ? upsert(new User(
//                        login.getUser().getId(),
//                        login.getPassword(),
//                        new User.ContactInfo(login.getUser().getContactInfo()),
//                        login.getUser().getZoneId(),
//                        login.getUser().getAuthorities(),
//                        login.getUser().getStories(),
//                        login.getUser().getTimestamp()
//                )) : new Response<>(null, HttpStatus.NOT_FOUND);
//    }
//
//    public static Response<User.DTO> upsert(User user) {
//        if (user == null)
//            return new Response<>(null, HttpStatus.NOT_FOUND);
//
//        return databaseInteraction(() -> {
//            User local = user.get_id() != null ? get(user.get_id()) : null;
//
//            try {
//                PasswordService.check(user.get_password(), local);
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//
//            return Response.ifNull(
//                    new User.DTO(UserBuilder.build(UserQueryService.upsert(
//                            new User(
//                                    local != null ? local.get_id() : null,
//                                    local != null ? local.get_password() : user.get_password(),
//                                    user.get_contactInfo(),
//                                    user.get_zoneId(),
//                                    local != null ? local.get_authorities() : user.get_authorities(),
//                                    user.get_history(),
//                                    local != null ? local.get_timestamp() : null
//                            )
//                    ))),
//                    HttpStatus.NOT_ACCEPTABLE,
//                    HttpStatus.CREATED
//            );
//        });
//    }

    public static Stream<UserDetails> getAllUserDetails() {
//        return getAllLogins()
//                .map(login -> org.springframework.security.core.userdetails.User.builder()
//                        .password(login.getPassword())
//                        .username(login.getUsername())
//                        .build()
//                );
//    }
    
//    public static Stream<Login> getAllLogins() {
//        return databaseInteraction(() -> UserBuilder.buildLogins(UserQueryService.getAllLogins()));
        return new ArrayList().stream();
    }
}
