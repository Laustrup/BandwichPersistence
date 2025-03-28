package laustrup.bandwichpersistence.core.managers;

import laustrup.bandwichpersistence.core.models.Login;
import laustrup.bandwichpersistence.core.models.Response;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.repositories.UserDetailsRepository;
import laustrup.bandwichpersistence.core.services.builders.UserBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;

import static laustrup.bandwichpersistence.core.managers.ManagerService.databaseInteraction;

public class UserDetailsManager {

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
                user = UserBuilder.build(UserDetailsRepository.getUser(login));
            } catch (Exception exception) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }

            return new Response<>(user, status);
        });
    }
}
