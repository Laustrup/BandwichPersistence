package laustrup.bandwichpersistence.core.managers;

import laustrup.bandwichpersistence.core.models.Login;
import laustrup.bandwichpersistence.core.models.Response;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.repositories.UserDetailsRepository;
import laustrup.bandwichpersistence.core.services.builders.UserBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;
import java.util.stream.Stream;

import static laustrup.bandwichpersistence.core.managers.ManagerService.databaseInteraction;

public class UserDetailsManager {

    public static Stream<UserDetails> getUserDetails() {
        return databaseInteraction(() -> UserBuilder.buildLogins(UserDetailsRepository.getUserDetails()))
                .map(Function.identity());
    }

    public static Response<User> getUser(Login login) {
        return databaseInteraction(() -> {
            User user = null;
            HttpStatus status = HttpStatus.OK;

            try {
                user = UserBuilder.build(UserDetailsRepository.getUser(login));
            } catch (Exception exception) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }

            return new Response<>(user, status);
        });
    }
}
