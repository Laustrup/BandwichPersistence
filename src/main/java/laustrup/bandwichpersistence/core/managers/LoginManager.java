package laustrup.bandwichpersistence.core.managers;

import laustrup.bandwichpersistence.core.models.Login;
import laustrup.bandwichpersistence.core.models.Response;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.repositories.LoginRepository;
import laustrup.bandwichpersistence.core.services.builders.UserBuilder;
import org.springframework.http.HttpStatus;

import static laustrup.bandwichpersistence.core.managers.ManagerService.databaseInteraction;

public class LoginManager {

    public static Response<User> getResponse(Login login) {
        return databaseInteraction(() -> {
            User user = null;
            HttpStatus status = HttpStatus.OK;

            try {
                user = UserBuilder.build(LoginRepository.getUser(login));
            } catch (Exception exception) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }

            return new Response<>(user, status);
        });
    }
}
